package konkuk.shop.service;

import konkuk.shop.domain.item.repository.ItemRepository;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.domain.order.repository.OrderItemRepository;
import konkuk.shop.domain.review.application.ReviewService;
import konkuk.shop.domain.review.repository.ReviewRepository;
import konkuk.shop.dto.FindReviewDto;
import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.order.entity.OrderItem;
import konkuk.shop.domain.review.entity.Review;
import konkuk.shop.domain.review.dto.AddReviewForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class ReviewServiceTest {

    private final Long memberId = 1L;
    private final Long orderItemId = 10L;
    private final Long itemId = 14L;

    @Mock
    ReviewRepository reviewRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    OrderItemRepository orderItemRepository;
    @InjectMocks
    ReviewService reviewService;

    @Test
    @DisplayName("리뷰 저장하기 테스트")
    void saveReview() {
        //given
        OrderItem orderItem = new OrderItem(orderItemId, 5000, 3, false);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(new Member(0)));
        given(orderItemRepository.findById(orderItemId)).willReturn(Optional.of(orderItem));
        given(itemRepository.findById(itemId)).willReturn(Optional.of(new Item()));
        given(reviewRepository.save(any(Review.class))).willReturn(new Review());

        AddReviewForm form = new AddReviewForm("option", itemId, "description", 3, new ArrayList<>(), orderItemId);

        //when
        Long result = reviewService.saveReview(memberId, form);

        //then
        verify(memberRepository).findById(memberId);
        verify(orderItemRepository).findById(orderItemId);
        verify(itemRepository).findById(itemId);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    @DisplayName("상품 리뷰 찾기 테스트")
    void findReviewByItemId() {
        //given
        given(reviewRepository.findAllByItemId(itemId)).willReturn(new ArrayList<>());

        //when
        List<FindReviewDto> result = reviewService.findReviewByItemId(itemId);

        //then
        assertThat(result).isEmpty();
        verify(reviewRepository).findAllByItemId(itemId);
    }
}