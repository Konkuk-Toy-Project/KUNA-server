package konkuk.shop.entity;

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
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "adminMember")
    private List<Qna> qnas = new ArrayList<>();
    @OneToMany(mappedBy = "adminMember")
    private List<Item> items = new ArrayList<>();
}
