package konkuk.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.shop.WithAuthUser;
import konkuk.shop.domain.review.api.ReviewController;
import konkuk.shop.domain.review.dto.AddReviewForm;
import konkuk.shop.global.security.TokenProvider;
import konkuk.shop.domain.review.application.ReviewService;
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

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
    private final String email = "asdf@asdf.com";
    @MockBean
    ReviewService reviewService;
    @MockBean
    TokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("리뷰 추가 테스트")
    @WithAuthUser
    void addReview() throws Exception {
        given(reviewService.saveReview(any(Long.class), any(AddReviewForm.class))).willReturn(1L);

        String content = new ObjectMapper()
                .writeValueAsString(new AddReviewForm());

        mockMvc.perform(
                        post("/review")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reviewId").value(1))
                .andDo(print());

        verify(reviewService).saveReview(any(Long.class), any(AddReviewForm.class));
    }

    @Test
    @DisplayName("리뷰 찾기 테스트")
    void findReviewByItemId() throws Exception {
        given(reviewService.findReviewByItemId(any(Long.class))).willReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/review/3"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(reviewService).findReviewByItemId(any(Long.class));
    }
}