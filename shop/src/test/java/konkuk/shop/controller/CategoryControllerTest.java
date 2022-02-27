package konkuk.shop.controller;

import konkuk.shop.security.TokenProvider;
import konkuk.shop.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @MockBean
    CategoryService categoryService;
    @MockBean
    TokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("카테고리 조회 테스트")
    void findAllCategory() throws Exception {
        given(categoryService.findAll()).willReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/category"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(categoryService).findAll();
    }
}