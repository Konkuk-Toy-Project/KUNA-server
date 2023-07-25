package konkuk.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.shop.WithAuthUser;
import konkuk.shop.domain.cart.api.CartController;
import konkuk.shop.domain.cart.dto.RequestAddItemInCartForm;
import konkuk.shop.domain.cart.dto.RequestChangeCountForm;
import konkuk.shop.global.security.TokenProvider;
import konkuk.shop.domain.cart.application.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {
    private final String email = "asdf@asdf.com";
    @MockBean
    CartService cartService;
    @MockBean
    TokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("장바구니에 아이템 추가 테스트")
    @WithAuthUser
    void addItemInCart() throws Exception {
        given(cartService.addItem(any(Long.class), any(Long.class), any(Long.class), any(Long.class), any(Integer.class)))
                .willReturn(1L);

        List<RequestAddItemInCartForm> form = new ArrayList<>();
        form.add(new RequestAddItemInCartForm(1L, 1L, 1L, 3));
        String content = new ObjectMapper()
                .writeValueAsString(form);

        mockMvc.perform(
                        post("/cart")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(cartService).addItem(any(Long.class), any(Long.class), any(Long.class), any(Long.class), any(Integer.class));
    }

    @Test
    @DisplayName("장바구니 아이템 삭제 테스트")
    @WithAuthUser
    void deleteItemInCart() throws Exception {
        doNothing().when(cartService).deleteItemInCart(any(Long.class), any(Long.class));

        mockMvc.perform(
                        delete("/cart/3"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(cartService).deleteItemInCart(any(Long.class), any(Long.class));
    }

    @Test
    @DisplayName("장바구니 아이템 조회 테스트")
    @WithAuthUser
    void findAllByUserIdInCart() throws Exception {
        given(cartService.findAllByUserId(any(Long.class))).willReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/cart"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(cartService).findAllByUserId(any(Long.class));
    }

    @Test
    @DisplayName("장바구니 아이템 개수 수정 테스트")
    @WithAuthUser
    void changeCount() throws Exception {
        doNothing().when(cartService).changeCount(any(Long.class), any(Long.class), any(Integer.class));

        String content = new ObjectMapper()
                .writeValueAsString(new RequestChangeCountForm(3));

        mockMvc.perform(
                        put("/cart/3")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(status().isOk())
                .andDo(print());

        verify(cartService).changeCount(any(Long.class), any(Long.class), any(Integer.class));
    }
}