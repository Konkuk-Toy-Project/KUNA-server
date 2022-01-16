package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Qna {
    @Id
    @GeneratedValue
    @Column(name = "qna_id")
    private Long id;

    private boolean isSecret;
    private boolean isAnswered;
    private String question;
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="admin_member_id")
    private AdminMember adminMember;
}
