package konkuk.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.shop.WithAuthUser;
import konkuk.shop.form.requestForm.item.RequestAddItemDto;
import konkuk.shop.form.requestForm.item.RequestAddOptionForm;
import konkuk.shop.form.requestForm.item.ResponseItemDetail;
import konkuk.shop.security.TokenProvider;
import konkuk.shop.service.ItemService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
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
        given(itemService.addItem(any(Long.class), any(RequestAddItemDto.class))).willReturn(4L);

        String content = new ObjectMapper()
                .writeValueAsString(new RequestAddItemDto());

        mockMvc.perform(
                        post("/item")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(itemService).addItem(any(Long.class), any(RequestAddItemDto.class));
    }

    @Test
    @DisplayName("옵션 추가 테스트")
    @WithAuthUser(email = email)
    void registryOption() throws Exception {
        doNothing().when(itemService).saveOption(any(Long.class), eq(new ArrayList<>()), any(Long.class));

        String content = new ObjectMapper()
                .writeValueAsString(new RequestAddOptionForm());

        mockMvc.perform(
                        post("/item/3/option")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리로 아이템 찾기 테스트")
    void findItemListByCategory() throws Exception {
        given(itemService.findItemListByCategory(any(Long.class))).willReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/item/category/3"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemService).findItemListByCategory(any(Long.class));
    }

    @Test
    @DisplayName("상세 아이템 정보 요청 테스트")
    void findItemDetailByItemId() throws Exception {
        given(itemService.findItemById(any(Long.class))).willReturn(new ResponseItemDetail());

        mockMvc.perform(
                        get("/item/3"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemService).findItemById(any(Long.class));
    }

    @Test
    @DisplayName("모든 아이템 찾기 테스트")
    void findAllItem() throws Exception {
        given(itemService.findAllItem()).willReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/item"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemService).findAllItem();
    }

    @Test
    @DisplayName("검색어를 이용한 아이템 찾기 테스트")
    void findItemBySearchWord() throws Exception {
        given(itemService.findItemBySearchWord(any(String.class))).willReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/item/search/searchWord"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemService).findItemBySearchWord(any(String.class));
    }
}