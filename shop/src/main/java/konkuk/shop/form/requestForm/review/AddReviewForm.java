package konkuk.shop.form.requestForm.review;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AddReviewForm {
    String option;
    Long itemId;
    String description;
    Integer rate;
    List<MultipartFile> reviewImage;
    Long orderItemId;
}
