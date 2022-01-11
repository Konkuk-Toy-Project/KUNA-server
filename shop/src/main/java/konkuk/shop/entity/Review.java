package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Review {
    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    private Item item;
    private String option;
    private Member member;
    private String description;
    private Integer rate;
    private LocalDateTime registry_date;
    private List<ReviewImage> reviewImages = new ArrayList<>();
}
