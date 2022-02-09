package konkuk.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class InitItemDto {
    String name;
    Integer preference;
    Integer sale;
    Integer price;
    String category;
    String thumbnail;
    List<String> itemImages;
    List<String> detailImages;
    List<List<String>> optionName; // [[option1, option2, option2, ...], [option1, option2, ...] ...]
    List<List<Integer>> optionStock; // [[option1, option2, option2, ...], [option1, option2, ...] ...]
}
