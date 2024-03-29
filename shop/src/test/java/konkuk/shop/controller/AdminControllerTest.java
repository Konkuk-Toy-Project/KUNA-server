package konkuk.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.shop.WithAuthUser;
import konkuk.shop.domain.admin.api.AdminController;
import konkuk.shop.domain.admin.application.AdminManageItemService;
import konkuk.shop.domain.admin.dto.EditPriceAndSaleForm;
import konkuk.shop.domain.admin.dto.RequestAnswerQnaForm;
import konkuk.shop.domain.qna.application.QnaService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @MockBean
    private QnaService qnaService;

    @MockBean
    private AdminManageItemService adminManageItemService;

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("등록한 아아템 리스트 조회 테스트")
    @WithAuthUser
    void myItemList() throws Exception {
        given(adminManageItemService.findItemByUserId(any(Long.class))).willReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/admin/items"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(adminManageItemService).findItemByUserId(any(Long.class));
    }

    @Test
    @DisplayName("관리 아이템 qna 조회 테스트")
    @WithAuthUser
    void findQna() throws Exception {
        given(qnaService.findQnaByAdminMember(any(Long.class), any(Boolean.class)))
                .willReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/admin/qna/" + true))
                .andExpect(status().isOk())
                .andDo(print());

        verify(qnaService).findQnaByAdminMember(any(Long.class), any(Boolean.class));
    }

    @Test
    @DisplayName("qna 답변 등록 테스트")
    @WithAuthUser
    void saveAnswer() throws Exception {
        doNothing().when(qnaService).saveAnswer(any(Long.class), any(Long.class), any(String.class));

        String content = new ObjectMapper().writeValueAsString(new RequestAnswerQnaForm("answer"));

        mockMvc.perform(post("/admin/qna/" + 3)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(qnaService).saveAnswer(any(Long.class), any(Long.class), any(String.class));
    }

    @Test
    @DisplayName("상품 가격 조정 테스트")
    @WithAuthUser
    void editPriceByItemId() throws Exception {
        doNothing().when(adminManageItemService).editPriceByItemId(any(Long.class), any(Long.class), any(Integer.class), any(Integer.class));

        String content = new ObjectMapper().writeValueAsString(new EditPriceAndSaleForm(45000, 30));

        mockMvc.perform(put("/admin/price/" + 3)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(adminManageItemService).editPriceByItemId(any(Long.class), any(Long.class), any(Integer.class), any(Integer.class));
    }
}