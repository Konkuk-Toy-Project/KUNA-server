package konkuk.shop.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditPriceAndSaleForm {
    Integer price;
    Integer sale;
}
