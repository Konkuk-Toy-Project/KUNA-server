package konkuk.shop.form.responseForm.item;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ResponseItemListSimple {
    String itemState;
    MultipartFile thumbnail;
    String name;
}
