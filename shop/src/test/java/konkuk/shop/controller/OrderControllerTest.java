package konkuk.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.shop.WithAuthUser;
import konkuk.shop.dto.AddOrderDto;
import konkuk.shop.dto.FindOrderDto;
import konkuk.shop.form.requestForm.item.RequestAddItemDto;
import konkuk.shop.form.requestForm.order.RequestAddOrderForm;
import konkuk.shop.security.TokenProvider;
import konkuk.shop.service.ItemService;
import konkuk.shop.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @MockBean
    OrderService orderService;
    @MockBean
    TokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;

    private final String email = "asdf@asdf.com";

    @Test
    @DisplayName("주문하기 테스트")
    @WithAuthUser(email = email)
    void addOrder() throws Exception {
        given(orderService.addOrder(any(Long.class), any(RequestAddOrderForm.class))).willReturn(new AddOrderDto());

        String content = new ObjectMapper()
                .writeValueAsString(new RequestAddOrderForm());

        mockMvc.perform(
                        post("/order")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").hasJsonPath())
                .andExpect(jsonPath("$.totalPrice").hasJsonPath())
                .andExpect(jsonPath("$.shippingCharge").hasJsonPath())
                .andExpect(jsonPath("$.orderDate").hasJsonPath())
                .andExpect(jsonPath("$.usePoint").hasJsonPath())
                .andDo(print());

        verify(orderService).addOrder(any(Long.class), any(RequestAddOrderForm.class));
    }

    @Test
    @DisplayName("주문 상세정보 요청 테스트")
    @WithAuthUser(email = email)
    void getOrderDetail() throws Exception {
        given(orderService.findOrderDetailList(any(Long.class), any(Long.class))).willReturn(new FindOrderDto());

        mockMvc.perform(
                        get("/order/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").hasJsonPath())
                .andExpect(jsonPath("$.phone").hasJsonPath())
                .andExpect(jsonPath("$.recipient").hasJsonPath())
                .andExpect(jsonPath("$.deliveryState").hasJsonPath())
                .andExpect(jsonPath("$.orderDate").hasJsonPath())
                .andExpect(jsonPath("$.totalPrice").hasJsonPath())
                .andExpect(jsonPath("$.usedPoint").hasJsonPath())
                .andExpect(jsonPath("$.payMethod").hasJsonPath())
                .andExpect(jsonPath("$.shippingCharge").hasJsonPath())
                .andExpect(jsonPath("$.orderState").hasJsonPath())
                .andExpect(jsonPath("$.orderItems").hasJsonPath())
                .andExpect(jsonPath("$.orderId").hasJsonPath())
                .andDo(print());

        verify(orderService).findOrderDetailList(any(Long.class), any(Long.class));
    }

    @Test
    @DisplayName("주문 목록 요청 테스트")
    @WithAuthUser(email = email)
    void getOrderList() throws Exception {
        given(orderService.findOrderList(any(Long.class))).willReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/order"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(orderService).findOrderList(any(Long.class));
    }

    @Test
    @DisplayName("주문 상품 리스트 요청 테스트")
    @WithAuthUser(email = email)
    void getOrderItems() throws Exception {
        given(orderService.findOrderItemList(any(Long.class))).willReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/order/item"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(orderService).findOrderItemList(any(Long.class));
    }
}