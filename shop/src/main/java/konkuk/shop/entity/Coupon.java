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

    @Column(name="expired_date")
    private LocalDateTime expiredDate;

    @Column(name="coupon_condition")
    private String couponCondition;

    @Column(name="serial_number")
    private String serialNumber;

    private Integer rate;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private boolean isUsed;

    public Coupon(CouponKind couponKind, LocalDateTime expiredDate, String couponCondition, Integer rate, String name) {
        this.couponKind = couponKind;
        this.expiredDate = expiredDate;
        this.couponCondition = couponCondition;
        this.rate = rate;
        this.name = name;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
