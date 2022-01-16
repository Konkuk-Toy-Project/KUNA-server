package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class ItemImage {
    @Id
    @GeneratedValue
    @Column(name = "item_image_id")
    private Long id;

    private String upload_name;
    private String store_name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;
}
