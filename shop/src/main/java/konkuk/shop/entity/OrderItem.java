package konkuk.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @Column(name="item_price")
    private Integer itemPrice;

    @Column(name="item_name")
    private String itemName;

    @Column(name="item_version")
    private Integer itemVersion;

    private Integer count;
    private String thumbnailUrl;
    private String option1;
    private String option2;
    private boolean isReviewed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }
}
