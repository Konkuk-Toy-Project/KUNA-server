package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    private Member member;
    private Delivery delivery;
    @Column(name="order_date")
    private LocalDateTime orderDate;
    @Column(name="total_price")
    private Integer totalPrice;
    private Coupon coupon;
    @Column(name="used_point")
    private Integer usedPoint;
    @Column(name="pay_method")
    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;
    @Column(name="shipping_charge")
    private Integer shippingCharge;

    @Column(name="order_state")
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    private List<OrderItem> orderItems = new ArrayList<>();
}
