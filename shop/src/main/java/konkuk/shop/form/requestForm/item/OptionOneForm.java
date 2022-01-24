package konkuk.shop.form.requestForm.item;

import lombok.Data;

import java.util.List;

@Data
public class OptionOneForm {

    String name;
    Integer stock;
    List<OptionTwoForm> option2s;
}
