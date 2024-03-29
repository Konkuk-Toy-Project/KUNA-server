package konkuk.shop.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddItemInCartForm {
    Long itemId;
    Long option1Id;
    Long option2Id;
    Integer count;
}
