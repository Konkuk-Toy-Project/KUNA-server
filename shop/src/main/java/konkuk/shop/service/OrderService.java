package konkuk.shop.service;


import konkuk.shop.dto.AddOrderDto;
import konkuk.shop.dto.FindOrderDto;
import konkuk.shop.dto.FindOrderItemDto;
import konkuk.shop.dto.FindOrderListDto;
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));

        // 1. 쿠폰 검증
        Coupon coupon = validationCoupon(form.getCouponId(), form.getTotalPrice(), memberId);

        // 2. 배송 요금 확인
        if (form.getTotalPrice() >= 50000 && form.getShippingCharge() != 0)
            throw new ApiException(ExceptionEnum.INCORRECT_SHIPPING_CHARGE);
        if (form.getTotalPrice() < 50000 && form.getShippingCharge() == 0)
            throw new ApiException(ExceptionEnum.INCORRECT_SHIPPING_CHARGE);

        // 3. 재고 수량 확인 및 토탈 금액 검증
        List<OrderItem> orderItems = makeOrderItem(form.getOrderItems(), form.getTotalPrice());

        Delivery delivery = deliveryRepository.save(
                new Delivery(form.getAddress(), form.getPhone(), form.getRecipient(), DeliveryState.PREPARING));

        Order order = Order.builder()
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
                .build();

        Order saveOrder = orderRepository.save(order);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(saveOrder);
            orderItemRepository.save(orderItem);
        }

        if (coupon != null) coupon.setUsed(true);

        return new AddOrderDto(saveOrder.getId(), saveOrder.getTotalPrice(), saveOrder.getShippingCharge(), saveOrder.getOrderDate());
    }

    private List<OrderItem> makeOrderItem(List<OrderItemForm> orderItems, int totalPrice) {
        List<OrderItem> result = new ArrayList<>();
        int priceSum = 0;

        for (OrderItemForm orderItemform : orderItems) {
            Item item = itemRepository.findById(orderItemform.getItemId())
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));
            Option1 option1 = option1Repository.findById(orderItemform.getOption1Id())
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_OPTION1_BY_ID));

            OrderItem orderItem = OrderItem.builder()
                    .itemPrice(item.getPrice())
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
            priceSum += (item.getPrice() * orderItemform.getCount() * (100 - item.getSale()) * 0.01);
            result.add(orderItem);
        }

        if (priceSum != totalPrice) throw new ApiException(ExceptionEnum.INCORRECT_TOTAL_PRICE);
        return result;
    }

    private PayMethod convertPayMethod(String payMethod) {
        if (payMethod.equals("card")) return PayMethod.CARD;
        else if (payMethod.equals("bankbook")) return PayMethod.BANKBOOK;
        else throw new ApiException(ExceptionEnum.INCORRECT_PAYMENT_METHOD);
    }

    private Coupon validationCoupon(Long couponId, int totalPrice, Long userId) {
        if (couponId == null) return null;

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_COUPON));

        if (coupon.getMember().getId() != userId) throw new ApiException(ExceptionEnum.NOT_MATCH_COUPON_MEMBER);
        if (coupon.isUsed()) throw new ApiException(ExceptionEnum.ALREADY_USED_COUPON);
        if (coupon.getExpiredDate().isBefore(LocalDateTime.now()))
            throw new ApiException(ExceptionEnum.EXPIRED_COUPON);

        String couponCondition = coupon.getCouponCondition();
        String[] subStr = couponCondition.split("_");
        int conditionPrice = Integer.parseInt(subStr[2]);
        if (conditionPrice > totalPrice) throw new ApiException(ExceptionEnum.NOT_SATISFY_USE_COUPON);

        return coupon;
    }

    public FindOrderDto findOrderDetailList(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ORDER));
        if (order.getMember().getId() != userId) throw new ApiException(ExceptionEnum.NO_AUTHORITY_ACCESS_ORDER);

        List<FindOrderItemDto> itemDtos = new ArrayList<>();
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            FindOrderItemDto itemDto = FindOrderItemDto.builder()
                    .isReviewed(orderItem.isReviewed())
                    .itemId(orderItem.getItem().getId()) //이렇게 하면 item을 조회할텐데.. id만 따로 저장하면 안되나? 쿼리 낭비같은데
                    .name(orderItem.getItemName())
                    .option1(orderItem.getOption1())
                    .option2(orderItem.getOption2())
                    .thumbnailUrl(orderItem.getThumbnailUrl())
                    .price(orderItem.getItemPrice())
                    .count(orderItem.getCount())
                    .orderItemId(orderItem.getId())
                    .build();
            itemDtos.add(itemDto);
        }

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
        List<FindOrderListDto> result = orderRepository.findByMemberId(userId).stream()
                .map(e -> new FindOrderListDto(e.getOrderDate(), e.getTotalPrice(), e.getShippingCharge(),
                        e.getId(), e.getOrderState().toString(), e.getDelivery().getDeliveryState().toString()))
                .collect(Collectors.toList());
        return result;
    }
}
