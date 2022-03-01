package konkuk.shop.form.responseForm.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseMyItem {
    String itemState;
    String thumbnailUrl;
    String name;
    Integer price;
    Integer preference;
    Integer sale;
    Long itemId;
    Long categoryId;
    String categoryName;
}
