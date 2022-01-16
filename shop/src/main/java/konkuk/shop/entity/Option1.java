package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @OneToMany(mappedBy = "option1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option2> option2s = new ArrayList<>();
}
