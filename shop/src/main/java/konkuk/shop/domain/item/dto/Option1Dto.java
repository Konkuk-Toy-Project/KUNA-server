package konkuk.shop.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Option1Dto {
    private String name;
    private int stock;
    private Long option1Id;
    private List<Option2Dto> option2 = new ArrayList<>();
}
