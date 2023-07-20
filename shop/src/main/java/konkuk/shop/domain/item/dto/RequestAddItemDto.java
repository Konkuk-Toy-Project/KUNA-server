package konkuk.shop.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddItemDto {
    String name;
    Integer price;
    Integer sale;
    Long categoryId;
    MultipartFile thumbnail;
    List<MultipartFile> detailImages;
    List<MultipartFile> itemImages;
}


