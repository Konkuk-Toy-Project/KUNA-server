package konkuk.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CartItemDto {
    Long cartItemId;
    String thumbnailUrl;
    String option1;
    String option2;
    Integer price;
    Integer sale;
    Integer count;
}
