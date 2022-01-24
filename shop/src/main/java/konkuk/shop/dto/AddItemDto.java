package konkuk.shop.dto;

import konkuk.shop.entity.AdminMember;
import konkuk.shop.entity.CategoryItem;
import konkuk.shop.entity.Option1;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AddItemDto {
    String itemName;

    AdminMember adminMember;
    CategoryItem CategoryItem;
    List<Option1> option1;

    MultipartFile thumbnail;
    List<MultipartFile> detailImage;
    List<MultipartFile> itemImage;
}
