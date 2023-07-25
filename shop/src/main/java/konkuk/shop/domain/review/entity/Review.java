package konkuk.shop.domain.review.entity;

import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.image.entity.ReviewImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @Column(name = "registry_date")
    private LocalDateTime registryDate;

    private String option;
    private String description;
    private Integer rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();
}
