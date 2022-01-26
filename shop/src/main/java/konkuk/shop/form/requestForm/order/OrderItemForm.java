package konkuk.shop.form.requestForm.order;

import lombok.Data;

@Data
public class OrderItemForm {
    Long itemId;
    Long option1Id;
    Long option2Id;
    Integer count;
}
