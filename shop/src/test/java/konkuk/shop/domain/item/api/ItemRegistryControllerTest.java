package konkuk.shop.domain.item.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.shop.WithAuthUser;
import konkuk.shop.domain.item.application.ItemService;
import konkuk.shop.domain.item.dto.ItemAddDto;
import konkuk.shop.global.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRegistryController.class)
class ItemRegistryControllerTest {
    private final String email = "asdf@asdf.com";

    @MockBean
    ItemService itemService;

    @MockBean
    TokenProvider tokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("아이템 등록 테스트")
    @WithAuthUser(email = email)
    void registryItem() throws Exception {
        given(itemService.addItem(any(Long.class), any(ItemAddDto.Request.class))).willReturn(4L);

        String content = new ObjectMapper()
                .writeValueAsString(new ItemAddDto.Request());

        mockMvc.perform(
                        post("/item")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemId").value(4))
                .andDo(print());

        verify(itemService).addItem(any(Long.class), any(ItemAddDto.Request.class));
    }
}