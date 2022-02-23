package konkuk.shop.dto;

import konkuk.shop.entity.AdminMember;
import konkuk.shop.entity.Category;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class AddItemDto {
    String itemName;
    Integer price;
    Integer sale;

    AdminMember adminMember;
    Category category;

    MultipartFile thumbnail;
    List<MultipartFile> detailImage;
    List<MultipartFile> itemImage;
}
