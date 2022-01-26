package konkuk.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @Column(name="order_date")
    private LocalDateTime orderDate;

    @Column(name="total_price")
    private Integer totalPrice;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
}
