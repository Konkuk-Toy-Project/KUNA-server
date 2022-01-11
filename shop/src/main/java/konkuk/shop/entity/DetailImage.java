package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
@NoArgsConstructor
@Getter
public class DetailImage {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    private Item item;
    private String upload_name;
    private String store_name;
}
