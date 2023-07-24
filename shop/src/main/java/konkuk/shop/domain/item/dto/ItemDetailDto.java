package konkuk.shop.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailDto {
    private String itemState;
    private List<String> itemImageUrl;
    private String name;
    private int price;
    private int preference;
    private LocalDateTime registryDate;
    private int sale;
    private List<String> DetailImageUrl;
    private Long itemId;
    private String categoryName;
    private Long categoryId;
    private List<Option1Dto> option1 = new ArrayList<>();
    private String thumbnailUrl;
}
