package konkuk.shop.domain.image.entity;

import konkuk.shop.domain.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class ReviewImage {
    @Id
    @GeneratedValue
    @Column(name = "review_image_id")
    private Long id;

    private String upload_name;
    private String store_name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="review_id")
    private Review review;

    public ReviewImage(String upload_name, String store_name, Review review) {
        this.upload_name = upload_name;
        this.store_name = store_name;
        this.review = review;
    }
}
