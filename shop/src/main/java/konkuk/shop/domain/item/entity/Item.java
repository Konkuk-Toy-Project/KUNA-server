package konkuk.shop.domain.item.entity;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.category.entity.Category;
import konkuk.shop.domain.image.entity.DetailImage;
import konkuk.shop.domain.image.entity.ItemImage;
import konkuk.shop.domain.image.entity.Thumbnail;
import konkuk.shop.domain.qna.entity.Qna;
import konkuk.shop.domain.review.entity.Review;
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
public class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(name = "preference_count")
    private Integer preferenceCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_state")
    private ItemState itemState;

    @Column(name = "registry_date")
    private LocalDateTime registryDate;

    private Integer version;
    private Integer sale; //% 할인
    private Integer price;
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_id")
    private Thumbnail thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_member_id")
    private AdminMember adminMember;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Qna> qna = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> itemImages = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option1> option1s = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailImage> detailImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public static Item initialItemToRegistry(String name, Integer price, Integer sale, Category category, AdminMember adminMember) {
        return Item.builder()
                .itemState(ItemState.NORMALITY)
                .adminMember(adminMember)
                .name(name)
                .preferenceCount(0)
                .registryDate(LocalDateTime.now())
                .version(1)
                .sale(sale)
                .price(price)
                .category(category)
                .build();
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void plusPreferenceCount() {
        if (this.preferenceCount == null) this.preferenceCount = 0; // for test
        this.preferenceCount += 1;
    }

    public void minusPreferenceCount() {
        if (this.preferenceCount == null) this.preferenceCount = 0; // for test
        this.preferenceCount -= 1;
    }

    public void changePriceAndSale(Integer price, Integer sale) {
        if (price != null) this.price = price;
        if (sale != null) this.sale = sale;
        this.version += 1;
    }

    // test용
    public Item(Long id) {
        this.id = id;
    }
}

