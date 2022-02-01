package konkuk.shop.form.requestForm.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ResponseItemDetail {
    String itemState;
    List<String> itemImageUrl;
    String name;
    Integer price;
    Integer preference;
    LocalDateTime registryDate;
    Integer sale;
    List<String> DetailImageUrl;
    Long itemId;
    String categoryName;
    Long categoryId;
    List<Option1Dto> option1=new ArrayList<>();
}
