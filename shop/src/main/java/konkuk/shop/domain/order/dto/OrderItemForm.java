package konkuk.shop.domain.order.dto;

import lombok.Data;

@Data
public class OrderItemForm {
    Long itemId;
    Long option1Id;
    Long option2Id;
    Integer count;
}
