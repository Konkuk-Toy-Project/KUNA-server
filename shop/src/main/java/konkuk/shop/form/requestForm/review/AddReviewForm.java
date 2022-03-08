package konkuk.shop.form.requestForm.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewForm {
    String option;
    Long itemId;
    String description;
    Integer rate;
    List<MultipartFile> reviewImage;
    Long orderItemId;
}
