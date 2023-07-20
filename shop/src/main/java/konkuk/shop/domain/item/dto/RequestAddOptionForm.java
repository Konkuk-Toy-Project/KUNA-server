package konkuk.shop.domain.item.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestAddOptionForm {
    List<OptionOneForm> option1s;
}
