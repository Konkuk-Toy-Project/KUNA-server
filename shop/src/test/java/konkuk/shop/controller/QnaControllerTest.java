package konkuk.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.shop.WithAuthUser;
import konkuk.shop.form.requestForm.qna.RequestAddQnaForm;
import konkuk.shop.security.TokenProvider;
import konkuk.shop.service.QnaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QnaController.class)
class QnaControllerTest {
    @MockBean
    QnaService qnaService;
    @MockBean
    TokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;
    private final String email = "asdf@asdf.com";

    @Test
    @DisplayName("Qna 등록 테스트")
    @WithAuthUser(email = email)
    void addQna() throws Exception {
        given(qnaService.saveQna(any(Long.class), any(RequestAddQnaForm.class))).willReturn(1L);

        String content = new ObjectMapper()
                .writeValueAsString(new RequestAddQnaForm());

        mockMvc.perform(
                        post("/qna")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qnaId").value(1))
                .andDo(print());

        verify(qnaService).saveQna(any(Long.class), any(RequestAddQnaForm.class));
    }

    @Test
    @DisplayName("Qna 찾기 테스트")
    @WithAuthUser(email = email)
    void findQnaByItemId() throws Exception {
        given(qnaService.findQnaByItemId(any(Long.class), any(Long.class))).willReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/qna/3"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(qnaService).findQnaByItemId(any(Long.class), any(Long.class));
    }
}