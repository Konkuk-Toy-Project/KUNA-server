package konkuk.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FindOrderItemDto {
    String name;
    String option1;
    String option2;
    Integer price;
    String thumbnailUrl;
    boolean isReviewed;
    Long itemId;
    Integer count;
    Long orderItemId;
}
