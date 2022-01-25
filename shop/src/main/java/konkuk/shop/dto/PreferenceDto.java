package konkuk.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreferenceDto {
    String thumbnailUrl;
    String name;
    Integer price;
    Integer sale;
    Long preferenceId;
}
