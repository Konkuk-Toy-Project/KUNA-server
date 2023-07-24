package konkuk.shop.domain.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ItemAddDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String name;
        private Integer price;
        private Integer sale;
        private Long categoryId;
        private MultipartFile thumbnail;
        private List<MultipartFile> detailImages;
        private List<MultipartFile> itemImages;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long itemId;
    }
}


