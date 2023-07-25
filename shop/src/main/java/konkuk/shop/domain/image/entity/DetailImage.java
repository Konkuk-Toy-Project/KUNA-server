package konkuk.shop.domain.image.entity;

import konkuk.shop.domain.item.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;


@Entity
@NoArgsConstructor
@Getter
public class DetailImage {
    @Id
    @GeneratedValue
    @Column(name = "detail_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    private String upload_name;
    private String store_name;

    public DetailImage(String upload_name, String store_name, Item item) {
        this.item = item;
        this.upload_name = upload_name;
        this.store_name = store_name;
    }
}
