package konkuk.shop.domain.admin.entity;

import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.qna.entity.Qna;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class AdminMember {
    @Id
    @GeneratedValue
    @Column(name = "admin_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "adminMember")
    private List<Qna> qnas = new ArrayList<>();

    @OneToMany(mappedBy = "adminMember")
    private List<Item> items = new ArrayList<>();

    public AdminMember(Member member) {
        this.member = member;
    }
}
