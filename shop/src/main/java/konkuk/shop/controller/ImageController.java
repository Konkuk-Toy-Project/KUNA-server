package konkuk.shop.controller;

import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    @Value("${image.thumbnail}")
    private String thumbnailPath;

    @Value("${image.item}")
    private String itemPath;

    @Value("${image.detail}")
    private String detailPath;

    @Value("${image.review}")
    private String reviewPath;

    @GetMapping("/detail/{filename}")
    @ResponseBody
    public Resource detailImage(@PathVariable String filename) {
        log.info("detailImage 요청 filename={}", filename);
        UrlResource result;
        try {
            result = new UrlResource("file:" + detailPath + filename);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ExceptionEnum.FAIL_CALL_IMAGE);
        }
        return result;
    }

    @GetMapping("/item/{filename}")
    @ResponseBody
    public Resource itemImage(@PathVariable String filename) {
        log.info("itemImage 요청 filename={}", filename);
        UrlResource result;
        try {
            result = new UrlResource("file:" + itemPath + filename);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ExceptionEnum.FAIL_CALL_IMAGE);
        }
        return result;
    }

    @GetMapping("/thumbnail/{filename}")
    @ResponseBody
    public Resource thumbnail(@PathVariable String filename) {
        log.info("thumbnail 요청 filename={}", filename);
        UrlResource result;
        try {
            result = new UrlResource("file:" + thumbnailPath + filename);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ExceptionEnum.FAIL_CALL_IMAGE);
        }
        return result;
    }


    @GetMapping("/review/{filename}")
    @ResponseBody
    public Resource reviewImage(@PathVariable String filename) {
        log.info("reviewImage 요청 filename={}", filename);
        UrlResource result;
        try {
            result = new UrlResource("file:" + reviewPath + filename);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ExceptionEnum.FAIL_CALL_IMAGE);
        }
        return result;
    }
}
