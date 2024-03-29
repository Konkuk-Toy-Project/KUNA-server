package konkuk.shop.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OptionOneForm {

    String name;
    int stock;
    List<OptionTwoForm> option2s;
}
