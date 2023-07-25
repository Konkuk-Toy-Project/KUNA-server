package konkuk.shop.domain.item.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.shop.WithAuthUser;
import konkuk.shop.domain.item.application.OptionRegistryService;
import konkuk.shop.domain.item.dto.OptionAddDto;
import konkuk.shop.global.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OptionRegistryController.class)
class OptionRegistryControllerTest {

    private final String email = "asdf@asdf.com";

    @MockBean
    private OptionRegistryService optionRegistryService;

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("옵션 추가 테스트")
    @WithAuthUser
    void registryOption() throws Exception {
        doNothing().when(optionRegistryService).saveOption(any(Long.class), eq(new ArrayList<>()), any(Long.class));

        String content = objectMapper.writeValueAsString(new OptionAddDto());

        mockMvc.perform(post("/item/3/option")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andDo(print());
    }
}