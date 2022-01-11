package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Coupon {
    @Id
    @GeneratedValue
    @Column(name = "coupon_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="coupon_kind")
    private CouponKind couponKind;

    private Integer rate;
    @Column(name="expired_date")
    private LocalDateTime expiredDate;

    @Enumerated(EnumType.STRING)
    @Column(name="coupon_condition")
    private CouponCondition couponCondition;

    private String description;
    private String name;
    private Member member;
}
