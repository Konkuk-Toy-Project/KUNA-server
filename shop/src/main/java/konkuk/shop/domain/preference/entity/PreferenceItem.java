package konkuk.shop.domain.preference.entity;

import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class PreferenceItem {
    @Id
    @GeneratedValue
    @Column(name = "preference_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    public PreferenceItem(Member member, Item item) {
        this.member = member;
        this.item = item;
    }

    // test 용
    public PreferenceItem(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
