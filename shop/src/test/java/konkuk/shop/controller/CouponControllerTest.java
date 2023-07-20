package konkuk.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.shop.WithAuthUser;
import konkuk.shop.domain.coupon.api.CouponController;
import konkuk.shop.domain.coupon.dto.RequestAddCouponForm;
import konkuk.shop.domain.coupon.dto.ResponseGetCoupon;
import konkuk.shop.global.security.TokenProvider;
import konkuk.shop.domain.coupon.application.CouponService;
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

@WebMvcTest(CouponController.class)
class CouponControllerTest {
    @MockBean
    CouponService couponService;
    @MockBean
    TokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;

    private final String email = "asdf@asdf.com";

    @Test
    @DisplayName("관리자가 쿠폰 등록 테스트")
    void addCoupon() throws Exception {
        given(couponService.saveCoupon(any(RequestAddCouponForm.class))).willReturn("serialNumber");

        String content = new ObjectMapper()
                .writeValueAsString(new RequestAddCouponForm());

        mockMvc.perform(
                        post("/coupon")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("serialNumber"))
                .andDo(print());

        verify(couponService).saveCoupon(any(RequestAddCouponForm.class));
    }

    @Test
    @DisplayName("사용자가 쿠폰 등록 테스트")
    @WithAuthUser(email = email)
    void registryCoupon() throws Exception {
        given(couponService.registryCoupon(any(Long.class), any(String.class))).willReturn(new ResponseGetCoupon());

        mockMvc.perform(
                        post("/coupon/user")
                                .content("{\"serialNumber\": \"a1b2c3-d4e5\"}")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.couponKind").hasJsonPath())
                .andExpect(jsonPath("$.rate").hasJsonPath())
                .andExpect(jsonPath("$.expiredDate").hasJsonPath())
                .andExpect(jsonPath("$.couponCondition").hasJsonPath())
                .andExpect(jsonPath("$.name").hasJsonPath())
                .andExpect(jsonPath("$.isUsed").hasJsonPath())
                .andExpect(jsonPath("$.couponId").hasJsonPath())
                .andDo(print());

        verify(couponService).registryCoupon(any(Long.class), any(String.class));
    }

    @Test
    @DisplayName("쿠폰 조회 테스트")
    @WithAuthUser(email = email)
    void getCoupon() throws Exception {
        given(couponService.getCoupon(any(Long.class))).willReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/coupon"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(couponService).getCoupon(any(Long.class));
    }
}