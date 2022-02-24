package konkuk.shop.service;


import konkuk.shop.dto.*;
import konkuk.shop.entity.*;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.form.requestForm.order.OrderItemForm;
import konkuk.shop.form.requestForm.order.RequestAddOrderForm;
import konkuk.shop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final Option1Repository option1Repository;
    private final Option2Repository option2Repository;
    private final CouponRepository couponRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public AddOrderDto addOrder(Long memberId, RequestAddOrderForm form) {
        log.info("주문 요청. memberId={}", memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));

        // 1. 쿠폰 검증
        Coupon coupon = validationCoupon(form.getCouponId(), memberId);

        // 2. 배송 요금 확인
        validationShippingCharge(form.getTotalPrice(), form.getShippingCharge());

        // 3. 포인트 체크
        if (member.getPoint() < form.getUsePoint()) throw new ApiException(ExceptionEnum.NOT_ENOUGH_POINTS);

        // 4. 재고 수량 확인 및 토탈 금액 검증 + 쿠폰 사용 조건 검증
        List<OrderItem> orderItems = makeOrderItem(form.getOrderItems(), form.getTotalPrice(), coupon);

        Delivery delivery = deliveryRepository.save(
                new Delivery(form.getAddress(), form.getPhone(), form.getRecipient(), DeliveryState.PREPARING));

        // 회원 배송지 저장
        member.setAddress(form.getAddress());

        Order saveOrder = orderRepository.save(Order.builder()
                .delivery(delivery)
                .member(member)
                .orderDate(LocalDateTime.now())
                .totalPrice(form.getTotalPrice())
                .coupon(coupon)
                .usedPoint(form.getUsePoint())
                .payMethod(convertPayMethod(form.getPayMethod()))
                .shippingCharge(form.getShippingCharge())
                .orderState(OrderState.NORMALITY)
                .orderItems(orderItems)
                .build());

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(saveOrder);
            orderItemRepository.save(orderItem);
        }

        if (coupon != null) coupon.setUsed(true);

        member.changePoint(-form.getUsePoint());
        member.changePoint((int) (form.getTotalPrice() * 0.01));

        return new AddOrderDto(saveOrder.getId(), saveOrder.getTotalPrice(),
                saveOrder.getShippingCharge(), saveOrder.getOrderDate(), saveOrder.getUsedPoint());
    }

    private void validationShippingCharge(Integer totalPrice, Integer shippingCharge) {
        if (totalPrice >= 50000 && shippingCharge != 0)
            throw new ApiException(ExceptionEnum.INCORRECT_SHIPPING_CHARGE);
        if (totalPrice < 50000 && shippingCharge == 0)
            throw new ApiException(ExceptionEnum.INCORRECT_SHIPPING_CHARGE);
    }

    private List<OrderItem> makeOrderItem(List<OrderItemForm> orderItems, int totalPrice, Coupon coupon) {
        List<OrderItem> result = new ArrayList<>();
        int salePriceSum = 0;

        for (OrderItemForm orderItemform : orderItems) {
            Item item = itemRepository.findById(orderItemform.getItemId())
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));
            Option1 option1 = option1Repository.findById(orderItemform.getOption1Id())
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_OPTION1_BY_ID));

            int itemPrice = Integer.parseInt(String.valueOf(Math.round((100 - item.getSale()) * 0.01 * item.getPrice())));
            OrderItem orderItem = OrderItem.builder()
                    .itemPrice(itemPrice) // 세일이 적용된 가격, 1개당 가격
                    .itemName(item.getName())
                    .itemVersion(item.getVersion())
                    .option1(option1.getName())
                    .count(orderItemform.getCount())
                    .thumbnailUrl(item.getThumbnail().getStore_name())
                    .isReviewed(false)
                    .item(item)
                    .build();

            if (option1.getStock() < orderItemform.getCount())
                throw new ApiException(ExceptionEnum.NO_STOCK_ITEM);
            option1.minusStock(orderItemform.getCount());

            if (orderItemform.getOption2Id() != null) {
                Option2 option2 = option2Repository.findById(orderItemform.getOption2Id())
                        .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_OPTION2_BY_ID));

                if (option2.getStock() < orderItemform.getCount())
                    throw new ApiException(ExceptionEnum.NO_STOCK_ITEM);
                option2.minusStock(orderItemform.getCount());

                orderItem.setOption2(option2.getName());
            }
            salePriceSum += (itemPrice * orderItemform.getCount());
            result.add(orderItem);
        }

        int validTotalPrice = salePriceSum;

        if (coupon != null) {
            // 쿠폰 사용 조건 검증
            String couponCondition = coupon.getCouponCondition();
            String[] subStr = couponCondition.split("_");
            int conditionPrice = Integer.parseInt(subStr[2]);
            if (conditionPrice > salePriceSum) throw new ApiException(ExceptionEnum.NOT_SATISFY_USE_COUPON);

            if (coupon.getCouponKind().equals(CouponKind.STATIC)) {
                if (salePriceSum > coupon.getRate()) validTotalPrice = salePriceSum - coupon.getRate();
                else throw new ApiException(ExceptionEnum.NOT_HIGHER_PRICE_THAN_COUPON);
            } else if (coupon.getCouponKind().equals(CouponKind.PERCENT)) {
                int salePriceByCoupon = (int) (salePriceSum * (100 - coupon.getRate()) * 0.01);
                validTotalPrice = salePriceSum - salePriceByCoupon;
            }
        }

        if (validTotalPrice != totalPrice) {
            log.info("Incorrect total price!! request totalPrice={},  priceSum={}", totalPrice, validTotalPrice);
            throw new ApiException(ExceptionEnum.INCORRECT_TOTAL_PRICE);
        }
        return result;
    }

    private PayMethod convertPayMethod(String payMethod) {
        if (payMethod.equals("card")) return PayMethod.CARD;
        else if (payMethod.equals("bankbook")) return PayMethod.BANKBOOK;
        else throw new ApiException(ExceptionEnum.INCORRECT_PAYMENT_METHOD);
    }

    private Coupon validationCoupon(Long couponId, Long userId) {
        if (couponId == null) return null;

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_COUPON));

        if (!coupon.getMember().getId().equals(userId)) throw new ApiException(ExceptionEnum.NOT_MATCH_COUPON_MEMBER);
        if (coupon.isUsed()) throw new ApiException(ExceptionEnum.ALREADY_USED_COUPON);
        if (coupon.getExpiredDate().isBefore(LocalDateTime.now()))
            throw new ApiException(ExceptionEnum.EXPIRED_COUPON);

        return coupon;
    }

    public FindOrderDto findOrderDetailList(Long userId, Long orderId) {
        log.info("주문 상세 조회. memberId={}, orderId={}", userId, orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ORDER));
        if (!order.getMember().getId().equals(userId)) throw new ApiException(ExceptionEnum.NO_AUTHORITY_ACCESS_ORDER);

        List<FindOrderItemDto> itemDtos = order.getOrderItems().stream()
                .map(orderItem ->
                        FindOrderItemDto.builder()
                                .isReviewed(orderItem.isReviewed())
                                .itemId(orderItem.getItem().getId())
                                .name(orderItem.getItemName())
                                .option1(orderItem.getOption1())
                                .option2(orderItem.getOption2())
                                .thumbnailUrl(orderItem.getThumbnailUrl())
                                .price(orderItem.getItemPrice())
                                .count(orderItem.getCount())
                                .orderItemId(orderItem.getId())
                                .build()
                ).collect(Collectors.toList());

        Delivery delivery = order.getDelivery();

        return FindOrderDto.builder()
                .orderId(order.getId())
                .address(delivery.getAddress())
                .phone(delivery.getPhone())
                .recipient(delivery.getRecipient())
                .deliveryState(delivery.getDeliveryState().toString())
                .orderItems(itemDtos)
                .orderState(order.getOrderState().toString())
                .orderDate(order.getOrderDate())
                .usedPoint(order.getUsedPoint())
                .totalPrice(order.getTotalPrice())
                .payMethod(order.getPayMethod().toString())
                .shippingCharge(order.getShippingCharge())
                .build();
    }

    public List<FindOrderListDto> findOrderList(Long userId) {
        log.info("주문 목록 조회. memberId={}", userId);
        return orderRepository.findByMemberId(userId).stream()
                .map(e -> new FindOrderListDto(e.getOrderDate(), e.getTotalPrice(), e.getShippingCharge(),
                        e.getId(), e.getOrderState().toString(), e.getDelivery().getDeliveryState().toString()))
                .collect(Collectors.toList());
    }

    public List<OrderItemDto> findOrderItemList(Long userId) {
        List<OrderItemDto> result = new ArrayList<>();

        orderRepository.findByMemberId(userId)
                .forEach(order -> {
                    List<OrderItem> orderItems = order.getOrderItems();
                    for (OrderItem orderItem : orderItems) {
                        result.add(new OrderItemDto(orderItem.getItemName(), orderItem.isReviewed(), orderItem.getItem().getId(),
                                orderItem.getOption1() + "/" + orderItem.getOption2(), orderItem.getId()));
                    }
                });

        log.info("주문 상품 조회. memberId={}", userId);
        return result;
    }
}
