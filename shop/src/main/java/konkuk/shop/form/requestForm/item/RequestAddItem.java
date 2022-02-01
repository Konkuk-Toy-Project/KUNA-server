package konkuk.shop.form.requestForm.item;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RequestAddItem {
    String name;
    Integer price;
    Integer sale;
    Long categoryId;
    MultipartFile thumbnail;
    List<MultipartFile> detailImages;
    List<MultipartFile> itemImages;
}


