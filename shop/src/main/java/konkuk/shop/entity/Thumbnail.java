package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public Thumbnail(String upload_name, String store_name) {
        this.upload_name = upload_name;
        this.store_name = store_name;
    }
}
