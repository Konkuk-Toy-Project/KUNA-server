package konkuk.shop.service;


import konkuk.shop.entity.*;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.form.requestForm.order.OrderItemForm;
import konkuk.shop.form.requestForm.order.RequestAddOrderForm;
import konkuk.shop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public void addOrder(Long memberId, RequestAddOrderForm form) {
        /**
         * 쿠폰 사용 조건
         * 주문 토탈 금액 확인
         * 배송 요금 확인
         * 등등 많은 검증이 필요함. 화이팅!
         */
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        Coupon coupon = couponRepository.findById(form.getCouponId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_COUPON));
        coupon.setUsed(true);
        couponRepository.save(coupon);

        Delivery delivery = deliveryRepository.save(new Delivery(form.getAddress(), form.getPhone(), form.getRecipient(), DeliveryState.PREPARING));

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

            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .order(saveOrder)
                    .itemPrice(item.getPrice())
                    .itemName(item.getName())
                    .itemVersion(item.getVersion())
                    .option1(option1.getName())
                    .option2(option2.getName())
                    .count(orderItemform.getCount())
                    .thumbnailUrl(item.getThumbnail().getStore_name())
                    .isReviewed(false)
                    .build();

            saveOrder.getOrderItems().add(orderItemRepository.save(orderItem));
        }
    }

    private PayMethod convertPayMethod(String payMethod){
        if(payMethod.equals("card")) return PayMethod.CARD;
        else if(payMethod.equals("bankbook")) return PayMethod.BANKBOOK;
        else throw new ApiException(ExceptionEnum.INCORRECT_PAYMENT_METHOD);
    }
}
