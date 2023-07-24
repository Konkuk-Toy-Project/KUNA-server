package konkuk.shop.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ItemInfoDto {
    private String itemState;
    private String thumbnailUrl;
    private String name;
    private int price;
    private int preference;
    private int sale;
    private Long itemId;
    private Long categoryId;
    private String categoryName;
}
