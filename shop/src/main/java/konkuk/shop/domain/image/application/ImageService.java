package konkuk.shop.domain.image.application;

import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    @Value("${image.thumbnail}")
    private String thumbnailPath;

    @Value("${image.item}")
    private String itemPath;

    @Value("${image.detail}")
    private String detailPath;

    @Value("${image.review}")
    private String reviewPath;

    public UrlResource getImageFile(String type, String fileName) {
        log.info("{} 요청 filename={}", type + "Image", fileName);
        try {
            return new UrlResource("file:" + typeConvert(type) + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(ErrorCode.FAIL_CALL_IMAGE);
        }
    }

    private String typeConvert(String type) {
        switch (type) {
            case "detail":
                return detailPath;
            case "item":
                return itemPath;
            case "review":
                return reviewPath;
            case "thumbnail":
                return thumbnailPath;
            default:
                throw new ApplicationException(ErrorCode.FAIL_CALL_IMAGE);
        }
    }
}
