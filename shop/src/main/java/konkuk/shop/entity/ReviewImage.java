package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
