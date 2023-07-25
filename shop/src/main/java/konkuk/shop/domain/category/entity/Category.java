package konkuk.shop.domain.category.entity;

import konkuk.shop.domain.item.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Item> items = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }
}
