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
public class ReviewImage {
    @Id
    @GeneratedValue
    @Column(name = "review_image_id")
    private Long id;

    private Review review;
    private String upload_name;
    private String store_name;
}
