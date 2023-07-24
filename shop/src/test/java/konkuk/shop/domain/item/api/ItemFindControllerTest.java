package konkuk.shop.domain.item.api;

import konkuk.shop.domain.item.application.ItemService;
import konkuk.shop.domain.item.dto.ItemDetailDto;
import konkuk.shop.global.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemFindController.class)
class ItemFindControllerTest {

    @MockBean
    ItemService itemService;

    @MockBean
    TokenProvider tokenProvider;

    @Autowired
    private MockMvc mockMvc;

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
        given(itemService.findItemById(any(Long.class))).willReturn(new ItemDetailDto());

        mockMvc.perform(
                        get("/item/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemState").hasJsonPath())
                .andExpect(jsonPath("$.name").hasJsonPath())
                .andExpect(jsonPath("$.price").hasJsonPath())
                .andExpect(jsonPath("$.preference").hasJsonPath())
                .andExpect(jsonPath("$.registryDate").hasJsonPath())
                .andExpect(jsonPath("$.sale").hasJsonPath())
                .andExpect(jsonPath("$.itemId").hasJsonPath())
                .andExpect(jsonPath("$.categoryName").hasJsonPath())
                .andExpect(jsonPath("$.categoryId").hasJsonPath())
                .andExpect(jsonPath("$.thumbnailUrl").hasJsonPath())
                .andExpect(jsonPath("$.itemImageUrl").hasJsonPath())
                .andExpect(jsonPath("$.detailImageUrl").hasJsonPath())
                .andExpect(jsonPath("$.option1").hasJsonPath())
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