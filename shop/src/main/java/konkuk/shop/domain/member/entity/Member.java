package konkuk.shop.domain.member.entity;

import konkuk.shop.domain.cart.entity.CartItem;
import konkuk.shop.domain.coupon.entity.Coupon;
import konkuk.shop.domain.order.entity.Order;
import konkuk.shop.domain.preference.entity.PreferenceItem;
import konkuk.shop.domain.qna.entity.Qna;
import konkuk.shop.domain.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    private MemberRole memberRole;

    private String email;
    private String password;
    private String name;
    private String phone; //01012345678
    private String birth; //2000/03/27
    private String address;
    private Integer point;
    private Integer chance;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreferenceItem> preferenceItems = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Qna> qnas = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();


    /**
     * 기본 회원가입 생성자
     * point, chance = 0,  Role = Bronze
     */
    public Member(String email, String password, String name, String phone, String birth) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
        this.point = 0;
        this.chance = 0;
    }

    public Member(Integer point) {
        this.point = point;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setMemberRole(MemberRole memberRole) {
        this.memberRole = memberRole;
    }

    public void changePoint(Integer point) {
        this.point += point;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}

