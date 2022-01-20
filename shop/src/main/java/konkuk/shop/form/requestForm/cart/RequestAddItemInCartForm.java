package konkuk.shop.form.requestForm.cart;

import lombok.Data;

@Data
public class RequestAddItemInCartForm {
    Long itemId;
    Long option1Id;
    Long option2Id;
    int count;
}
