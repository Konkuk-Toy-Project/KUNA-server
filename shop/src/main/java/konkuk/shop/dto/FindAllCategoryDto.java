package konkuk.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAllCategoryDto {
    Long categoryId;
    String categoryName;
}
