package konkuk.shop.dto;

import konkuk.shop.entity.AdminMember;
import konkuk.shop.entity.Category;
import konkuk.shop.entity.CategoryItem;
import konkuk.shop.entity.Option1;
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
    //CategoryItem CategoryItem;
    Category category;
    List<Option1> option1;

    MultipartFile thumbnail;
    List<MultipartFile> detailImage;
    List<MultipartFile> itemImage;
}
