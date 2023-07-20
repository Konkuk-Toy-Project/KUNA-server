package konkuk.shop.controller;

import konkuk.shop.WithAuthUser;
import konkuk.shop.domain.preference.api.PreferenceController;
import konkuk.shop.dto.IsPreference;
import konkuk.shop.dto.PreferenceDto;
import konkuk.shop.global.security.TokenProvider;
import konkuk.shop.domain.preference.application.PreferenceService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PreferenceController.class)
class PreferenceControllerTest {

    private final Long itemId = 14L;
    private final Long preferenceId = 20L;
    private final String email = "asdf@asdf.com";

    @MockBean
    PreferenceService preferenceService;
    @MockBean
    TokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("찜 상품 추가 테스트")
    @WithAuthUser(email = email)
    void addPreferenceItem() throws Exception {
        given(preferenceService.savePreferenceItem(any(Long.class), eq(itemId))).willReturn(preferenceId);

        //String content = new ObjectMapper().writeValueAsString(new RequestChangePasswordForm("changePassword"));

        mockMvc.perform(
                        post("/preference")
                                .content("{\"itemId\": " + itemId + "}")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preferenceId").value(preferenceId))
                .andDo(print());

        verify(preferenceService).savePreferenceItem(any(Long.class), eq(itemId));
    }

    @Test
    @DisplayName("찜 상품 조회 테스트")
    @WithAuthUser(email = email)
    void getPreferenceItem() throws Exception {
        given(preferenceService.findPreferenceByMemberId(any(Long.class))).willReturn(new ArrayList<PreferenceDto>());

        //String content = new ObjectMapper().writeValueAsString(new RequestChangePasswordForm("changePassword"));

        mockMvc.perform(get("/preference"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(preferenceService).findPreferenceByMemberId(any(Long.class));
    }

    @Test
    @DisplayName("찜 상품 삭제 테스트")
    @WithAuthUser(email = email)
    void deletePreferenceItem() throws Exception {
        doNothing().when(preferenceService).deletePreference(any(Long.class), any(Long.class));

        mockMvc.perform(delete("/preference/" + preferenceId))
                .andExpect(status().isOk())
                .andDo(print());

        verify(preferenceService).deletePreference(any(Long.class), any(Long.class));
    }

    @Test
    @DisplayName("해당 아이템이 찜 상품인지 확인 테스트")
    @WithAuthUser(email = email)
    void isPreferenceItem() throws Exception {
        given(preferenceService.isPreference(any(Long.class), eq(itemId))).willReturn(new IsPreference(true, true, preferenceId));

        mockMvc.perform(get("/preference/isPreference/" + itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preference").value(true))
                .andExpect(jsonPath("$.login").value(true))
                .andExpect(jsonPath("$.preferenceId").value(preferenceId))
                .andDo(print());

        verify(preferenceService).isPreference(any(Long.class), eq(itemId));
    }
}