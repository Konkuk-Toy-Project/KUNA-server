package konkuk.shop.form.requestForm.item;

import konkuk.shop.entity.ItemImage;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RequestAddItem {
    MultipartFile thumbnail;
    String name;
    Integer price;
    Integer preferenceCount;
    Integer sale;
    Long categoryId;
    List<MultipartFile> detailImage;
    List<MultipartFile> itemImages;
    List<OptionOne> option1s;

    public class OptionOne {
        String name;
        Integer stock;
        List<OptionTwo> option2s;
    }

    public class OptionTwo {
        String name;
        Integer stock;
    }
}


