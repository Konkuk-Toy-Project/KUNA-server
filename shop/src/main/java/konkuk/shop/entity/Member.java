package konkuk.shop.entity;

import lombok.Cleanup;
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

    private String password;
    private String name;
    private String phone; //01012345678
    private String birth; //2000/03/27

    @Enumerated(EnumType.STRING)
    @Column(name="member_role")
    private MemberRole memberRole;

    private String address;
    private Integer point;
    private Integer chance;

    private List<CartItem> cartItems = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private List<PreferenceItem> preferenceItems = new ArrayList<>();
    private List<Coupon> coupons = new ArrayList<>();
}
