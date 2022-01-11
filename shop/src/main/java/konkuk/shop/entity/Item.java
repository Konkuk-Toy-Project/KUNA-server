package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private List<Category> categories = new ArrayList<>();
    private Thumbnail thumbnail;
    private AdminMember adminMember;
    private String name;
    @Column(name="preference_count")
    private Integer preferenceCount;
    private List<DetailImage> detailImages = new ArrayList<>();
    private Integer version;
    private Integer sale;
    @Enumerated(EnumType.STRING)
    @Column(name="item_state")
    private ItemState itemState;
    @Column(name="registry_date")
    private LocalDateTime registryDate;
    private List<Qna> qna = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private List<ItemImage> itemImages = new ArrayList<>();
    private List<Option1> option1s = new ArrayList<>();
}
