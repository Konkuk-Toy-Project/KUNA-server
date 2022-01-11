package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    private Member member;
    private List<Qna> qnas = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
}
