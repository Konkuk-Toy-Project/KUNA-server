package konkuk.shop.service;

import konkuk.shop.dto.AddOrderDto;
import konkuk.shop.dto.FindOrderDto;
import konkuk.shop.dto.FindOrderListDto;
import konkuk.shop.dto.OrderItemDto;
import konkuk.shop.entity.*;
import konkuk.shop.form.requestForm.order.RequestAddOrderForm;
import konkuk.shop.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class OrderServiceTest {
    private final String address = "서울특별시";
    private final String recipient = "testMemberName";
    private final String phone = "01012345678";
    private final String payMethod = "card";
    private final Integer usePoint = 500;
    private final Integer totalPrice = 0; // 검증용
    private final Integer shippingCharge = 3000;
    private final Long memberId = 1L;
    private final Long orderId = 20L;
    @Mock
    ItemRepository itemRepository;
    @Mock
    AdminMemberRepository adminMemberRepository;
    @Mock
    Option1Repository option1Repository;
    @Mock
    Option2Repository option2Repository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    CouponRepository couponRepository;
    @Mock
    DeliveryRepository deliveryRepository;
    @Mock
    OrderItemRepository orderItemRepository;
    @InjectMocks
    OrderService orderService;

    @Test
    @DisplayName("주문 추가 테스트")
    void addOrder() {
        //given
        RequestAddOrderForm form = new RequestAddOrderForm(address, recipient, phone,
                payMethod, usePoint, totalPrice, shippingCharge, null, new ArrayList<>());
        Member member = new Member(usePoint);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(new Member()));
        given(deliveryRepository.save(any(Delivery.class))).willReturn(new Delivery());
        given(orderRepository.save(any(Order.class))).willReturn(new Order());

        //when
        AddOrderDto result = orderService.addOrder(memberId, form);

        //then
        assertThat(result.getTotalPrice()).isEqualTo(totalPrice);
        assertThat(result.getShippingCharge()).isEqualTo(shippingCharge);
        assertThat(result.getUsePoint()).isEqualTo(usePoint);

        verify(memberRepository).findById(memberId);
        verify(deliveryRepository).save(any(Delivery.class));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("주문 상세 정보 조회 테스트")
    void findOrderDetailList() {
        //given
        Member member = new Member(memberId);
        Order order = Order.builder()
                .member(member)
                .id(orderId)
                .payMethod(PayMethod.CARD)
                .usedPoint(usePoint)
                .totalPrice(totalPrice)
                .orderItems(new ArrayList<>())
                .shippingCharge(shippingCharge)
                .build();


        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        //when
        FindOrderDto result = orderService.findOrderDetailList(memberId, orderId);

        //then
        assertThat(result.getOrderId()).isEqualTo(orderId);
        assertThat(result.getShippingCharge()).isEqualTo(shippingCharge);
        assertThat(result.getUsedPoint()).isEqualTo(usePoint);
        assertThat(result.getTotalPrice()).isEqualTo(totalPrice);
        assertThat(result.getOrderItems()).isEmpty();
        assertThat(result.getPayMethod()).isEqualTo(payMethod);


        verify(orderRepository).findById(orderId);
    }

    @Test
    @DisplayName("주문 내역 조회 테스트")
    void findOrderList() {
        //given
        Member member = new Member(memberId);
        Order order = Order.builder()
                .member(member)
                .id(orderId)
                .payMethod(PayMethod.CARD)
                .usedPoint(usePoint)
                .totalPrice(totalPrice)
                .orderItems(new ArrayList<>())
                .shippingCharge(shippingCharge)
                .orderState(OrderState.NORMALITY)
                .delivery(new Delivery(address, phone, recipient, DeliveryState.PREPARING))
                .build();
        List<Order> list = new ArrayList<>();
        list.add(order);

        given(orderRepository.findByMemberId(memberId)).willReturn(list);

        //when
        List<FindOrderListDto> resultList = orderService.findOrderList(orderId);
        FindOrderListDto result = resultList.get(0);

        //then
        assertThat(result.getOrderId()).isEqualTo(orderId);
        assertThat(result.getShippingCharge()).isEqualTo(shippingCharge);
        assertThat(result.getTotalPrice()).isEqualTo(totalPrice);
        assertThat(result.getOrderState()).isEqualTo("NORMALITY");
        assertThat(result.getDeliveryState()).isEqualTo("PREPARING");
        assertThat(resultList).hasSize(1);


        verify(orderRepository).findByMemberId(memberId);
    }

    @Test
    @DisplayName("주문 상품 조회 테스트")
    void findOrderItemList() {
        //given
        Member member = new Member(memberId);
        Order order = Order.builder()
                .member(member)
                .id(orderId)
                .payMethod(PayMethod.CARD)
                .usedPoint(usePoint)
                .totalPrice(totalPrice)
                .orderItems(new ArrayList<>())
                .shippingCharge(shippingCharge)
                .build();
        List<Order> list = new ArrayList<>();
        list.add(order);

        given(orderRepository.findByMemberId(memberId)).willReturn(list);

        //when
        List<OrderItemDto> result = orderService.findOrderItemList(orderId);

        //then
        assertThat(result).hasSize(0);

        verify(orderRepository).findByMemberId(memberId);

    }
}