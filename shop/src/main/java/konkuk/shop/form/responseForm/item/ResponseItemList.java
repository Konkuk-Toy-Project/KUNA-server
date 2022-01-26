package konkuk.shop.form.responseForm.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseItemList {
    String itemState;
    String thumbnailUrl;
    String name;
    Integer price;
    Integer preferenceCount;
    Integer sale;
    Long itemId;
}
