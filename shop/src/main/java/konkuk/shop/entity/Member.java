package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @Column(name="member_role")
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
        this.memberRole = MemberRole.BRONZE;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
