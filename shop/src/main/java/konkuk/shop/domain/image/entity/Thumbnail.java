package konkuk.shop.domain.image.entity;

import konkuk.shop.domain.item.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Thumbnail {
    @Id
    @GeneratedValue
    @Column(name = "thumbnail_id")
    private Long id;

    private String upload_name;
    private String store_name;

    @OneToOne(mappedBy = "thumbnail")
    private Item item;

    public Thumbnail(String upload_name, String store_name, Item item) {
        this.upload_name = upload_name;
        this.store_name = store_name;
        this.item=item;
    }
}
