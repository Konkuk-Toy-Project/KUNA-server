package konkuk.shop.domain.item.dto;

import lombok.Data;

import java.util.List;

@Data
public class OptionOneForm {

    String name;
    Integer stock;
    List<OptionTwoForm> option2s;
}
