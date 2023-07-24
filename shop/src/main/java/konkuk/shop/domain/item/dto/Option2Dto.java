package konkuk.shop.domain.item.dto;

import konkuk.shop.domain.item.entity.Option2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Option2Dto {
    private String name;
    private int stock;
    private Long option2Id;

    public static List<Option2Dto> of(List<Option2> option2s) {
        return option2s.stream()
                .map(option2 -> Option2Dto.builder()
                        .name(option2.getName())
                        .stock(option2.getStock())
                        .option2Id(option2.getId())
                        .build())
                .toList();
    }
}
