package konkuk.shop.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AddItemDto {
    MultipartFile thumbnail;
    Long adminMemberId;
    String itemName;
    Long CategoryItemId;
    List<MultipartFile> detailImage;
    List<MultipartFile> itemImage;
    List<Long> option1Id;
}
