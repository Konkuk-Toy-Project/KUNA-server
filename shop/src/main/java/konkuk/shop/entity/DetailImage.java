package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter
public class DetailImage {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    private String upload_name;
    private String store_name;
}
