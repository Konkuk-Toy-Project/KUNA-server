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
import java.util.Optional;

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
        /**
         * 쿠폰 사용 조건
         * 주문 토탈 금액 확인
         * 배송 요금 확인
         * 재고 수량 확인
         * 등등 많은 검증이 필요함. 화이팅!
         */
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        Coupon coupon = useCoupon(form.getCouponId());

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
                .orderItems(new ArrayList<>())
                .build();

        Order saveOrder = orderRepository.save(order);

        List<OrderItemForm> orderItems = form.getOrderItems();
        for (OrderItemForm orderItemform : orderItems) {
            Item item = itemRepository.findById(orderItemform.getItemId())
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));
            Option1 option1 = option1Repository.findById(orderItemform.getOption1Id())
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_OPTION1_BY_ID));
            Option2 option2 = option2Repository.findById(orderItemform.getOption2Id())
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_OPTION2_BY_ID));

            /**
             * 옵션2가 없는 경우에 예외 처리 해줘야함.
             */
            option2.minusStock(orderItemform.getCount());

            OrderItem orderItem = OrderItem.builder()
                    .order(saveOrder)
                    .itemPrice(item.getPrice())
                    .itemName(item.getName())
                    .itemVersion(item.getVersion())
                    .option1(option1.getName())
                    .option2(option2.getName())
                    .count(orderItemform.getCount())
                    .thumbnailUrl(item.getThumbnail().getStore_name())
                    .isReviewed(false)
                    .item(item)
                    .build();

            saveOrder.getOrderItems().add(orderItemRepository.save(orderItem));
        }


        return new AddOrderDto(saveOrder.getId(), saveOrder.getTotalPrice(), saveOrder.getOrderDate());
    }

    private PayMethod convertPayMethod(String payMethod) {
        if (payMethod.equals("card")) return PayMethod.CARD;
        else if (payMethod.equals("bankbook")) return PayMethod.BANKBOOK;
        else throw new ApiException(ExceptionEnum.INCORRECT_PAYMENT_METHOD);
    }

    private Coupon useCoupon(Long couponId) {
        Optional<Coupon> couponOptional = couponRepository.findById(couponId);
        if (!couponOptional.isPresent()) return null;
        Coupon coupon = couponOptional.get();
        coupon.setUsed(true);
        return couponRepository.save(coupon);
    }

    public FindOrderDto findOrderDetailList(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ORDER));

        List<FindOrderItemDto> itemDtos = new ArrayList<>();
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            FindOrderItemDto itemDto = FindOrderItemDto.builder()
                    .isReviewed(orderItem.isReviewed())
                    .itemId(orderItem.getItem().getId()) //이렇게 하면 item을 조회할텐데.. id만 따로 저장하면 안되나? 쿼리 낭비같은데
                    .name(orderItem.getItemName())
                    .option1(orderItem.getOption1())
                    .option2(orderItem.getOption2())
                    .thumbnailIrl(orderItem.getThumbnailUrl())
                    .price(orderItem.getItemPrice())
                    .count(orderItem.getCount())
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
                .orderItemList(itemDtos)
                .orderState(order.getOrderState().toString())
                .orderDate(order.getOrderDate())
                .usedPoint(order.getUsedPoint())
                .totalPrice(order.getTotalPrice())
                .payMethod(order.getPayMethod().toString())
                .shippingCharge(order.getShippingCharge())
                .build();
    }

    public List<FindOrderListDto> findOrderList(Long userId) {
        List<Order> findOrder = orderRepository.findByMemberId(userId);

        List<FindOrderListDto> result = new ArrayList<>();
        for (Order order : findOrder) {
            result.add(new FindOrderListDto(order.getOrderDate(), order.getTotalPrice(), order.getId(),
                    order.getOrderState().toString(), order.getDelivery().getDeliveryState().toString()));
        }
        return result;
    }
}
