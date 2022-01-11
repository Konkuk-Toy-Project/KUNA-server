package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
public class Qna {
    @Id
    @GeneratedValue
    @Column(name = "qna_id")
    private Long id;

    private Item item;
    private Member member;
    private AdminMember adminMember;
    private boolean isSecret;
    private boolean isAnswered;
    private String question;
    private String answer;
}
