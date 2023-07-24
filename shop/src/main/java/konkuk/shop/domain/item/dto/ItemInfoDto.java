package konkuk.shop.domain.item.dto;

import konkuk.shop.domain.item.entity.Item;
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

    public static ItemInfoDto of(Item item) {
        return ItemInfoDto.builder()
                .itemState(item.getItemState().toString())
                .name(item.getName())
                .price(item.getPrice())
                .sale(item.getSale())
                .thumbnailUrl(item.getThumbnail().getStore_name())
                .preference(item.getPreferenceCount())
                .itemId(item.getId())
                .categoryId(item.getCategory().getId())
                .categoryName(item.getCategory().getName())
                .build();
    }
}
