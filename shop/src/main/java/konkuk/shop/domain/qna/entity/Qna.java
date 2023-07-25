package konkuk.shop.domain.qna.entity;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="admin_member_id")
    private AdminMember adminMember;

    @Column(name="registry_date")
    private LocalDateTime registryDate;

    @Column(name="answer_date")
    private LocalDateTime answerDate;


    public Qna(Item item, Member member, AdminMember adminMember, String question, boolean isSecret, String title) {
        this.isSecret = isSecret;
        this.question = question;
        this.item = item;
        this.member = member;
        this.adminMember = adminMember;
        this.registryDate = LocalDateTime.now();
        this.title = title;
    }

    public void registryAnswer(String answer) {
        this.answer = answer;
        this.isAnswered = true;
        this.answerDate = LocalDateTime.now();
    }
}
