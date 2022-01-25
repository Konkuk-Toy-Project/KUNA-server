package konkuk.shop.form.requestForm.cart;

import lombok.Data;

@Data
public class RequestChangeCountForm {
    Long cartItemId;
    Integer count;
}
