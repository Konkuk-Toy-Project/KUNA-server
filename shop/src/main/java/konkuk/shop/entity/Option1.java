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
public class Option1 {
    @Id
    @GeneratedValue
    @Column(name = "option1_id")
    private Long id;

    private Item item;
    private String name;
    private List<Option2> option2s = new ArrayList<>();
}
