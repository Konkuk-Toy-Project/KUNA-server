package konkuk.shop.domain.cart.entity;

import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.item.entity.Option1;
import konkuk.shop.domain.item.entity.Option2;
import konkuk.shop.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @Column(name = "item_version")
    private Integer itemVersion;

    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="option1_id")
    private Option1 option1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="option2_id")
    private Option2 option2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    public void setCount(Integer count) {
        this.count = count;
    }

    public CartItem(Long id) {
        this.id = id;
    }

    public CartItem(Long id, Member member) {
        this.id = id;
        this.member = member;
    }
}

