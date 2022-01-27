package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Option2 {
    @Id
    @GeneratedValue
    @Column(name = "option2_id")
    private Long id;

    private Integer stock;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="option1_id")
    private Option1 option1;

    public Option2(Integer stock, String name, Option1 option1) {
        this.stock = stock;
        this.name = name;
        this.option1 = option1;
    }

    public void minusStock(Integer count){
        this.stock-=count;
    }
}
