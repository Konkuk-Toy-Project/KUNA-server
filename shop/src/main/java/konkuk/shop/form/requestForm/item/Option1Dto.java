package konkuk.shop.form.requestForm.item;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Option1Dto {
    String name;
    Integer stock;
    Long option1Id;
    List<Option2Dto> option2 = new ArrayList<>();
}
