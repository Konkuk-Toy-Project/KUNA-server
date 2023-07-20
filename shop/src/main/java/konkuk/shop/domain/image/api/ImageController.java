package konkuk.shop.domain.image.api;

import konkuk.shop.domain.image.application.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{type}/{filename}")
    @ResponseBody
    public Resource image(@PathVariable String type, @PathVariable String filename) {
        return imageService.getImageFile(type, filename);
    }
}
