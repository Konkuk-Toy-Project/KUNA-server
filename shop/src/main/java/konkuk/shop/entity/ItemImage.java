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


    public ItemImage(String upload_name, String store_name, Item item) {
        this.upload_name = upload_name;
        this.store_name = store_name;
        this.item = item;
    }
}
