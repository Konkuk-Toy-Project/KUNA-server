package konkuk.shop.form.responseForm.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
