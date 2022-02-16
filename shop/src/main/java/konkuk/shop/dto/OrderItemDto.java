package konkuk.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDto {
    String itemName;
    Boolean isReviewed;
    Long itemId;
    String option;
    Long orderItemId;
}
