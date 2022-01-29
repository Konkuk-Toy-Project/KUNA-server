package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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


    public Qna(Item item, Member member, AdminMember adminMember, String question, boolean isSecret) {
        this.isSecret = isSecret;
        this.question = question;
        this.item = item;
        this.member = member;
        this.adminMember = adminMember;
        this.registryDate = LocalDateTime.now();
    }

    public void registryAnswer(String answer) {
        this.answer = answer;
        this.isAnswered = true;
        this.answerDate = LocalDateTime.now();
    }
}
