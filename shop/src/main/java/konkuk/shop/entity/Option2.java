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
public class Option2 {
    @Id
    @GeneratedValue
    @Column(name = "option2_id")
    private Long id;

    private Option1 option1;
    private Integer stock;
    private String name;
}
