package konkuk.shop.domain.item.dto;

import konkuk.shop.domain.item.entity.Option1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Option1Dto {
    private String name;
    private int stock;
    private Long option1Id;
    private List<Option2Dto> option2;

    public static Option1Dto of(Option1 option1) {
        List<Option2Dto> option2Dtoes = Option2Dto.of(option1.getOption2s());
        return Option1Dto.builder()
                .name(option1.getName())
                .stock(option1.getStock())
                .option1Id(option1.getId())
                .option2(option2Dtoes)
                .build();
    }
}
