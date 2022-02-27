package konkuk.shop.controller;

import konkuk.shop.security.TokenProvider;
import konkuk.shop.service.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
class ImageControllerTest {
    @MockBean
    ImageService imageService;
    @MockBean
    TokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("이미지 요청 테스트")
    void image() throws Exception {
        given(imageService.getImageFile(any(String.class), any(String.class))).willReturn(null);

        mockMvc.perform(
                        get("/image/type/filename"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(imageService).getImageFile(any(String.class), any(String.class));
    }
}