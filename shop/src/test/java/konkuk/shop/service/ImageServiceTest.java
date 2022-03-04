package konkuk.shop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.io.UrlResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class ImageServiceTest {

    @Test
    @DisplayName("파일 꺼내기 테스트")
    void getImageFile() {
        //given
        ImageService imageService = new ImageService();

        //when
        UrlResource result = imageService.getImageFile("thumbnail", "thumbnail1.webp");
    }
}