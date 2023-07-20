package konkuk.shop.domain.review.api;

import konkuk.shop.dto.FindReviewDto;
import konkuk.shop.domain.review.dto.AddReviewForm;
import konkuk.shop.domain.review.application.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> addReview(@AuthenticationPrincipal Long userId,
                                             AddReviewForm form) {
        Long reviewId = reviewService.saveReview(userId, form);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("reviewId", reviewId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<List<FindReviewDto>> findReviewByItemId(@PathVariable Long itemId) {
        List<FindReviewDto> reviewDtoList = reviewService.findReviewByItemId(itemId);

        return ResponseEntity.status(HttpStatus.OK).body(reviewDtoList);
    }
}
