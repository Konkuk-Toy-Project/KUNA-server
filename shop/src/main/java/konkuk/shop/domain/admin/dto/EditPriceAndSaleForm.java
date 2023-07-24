package konkuk.shop.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EditPriceAndSaleForm {
    Integer price;
    Integer sale;
}
