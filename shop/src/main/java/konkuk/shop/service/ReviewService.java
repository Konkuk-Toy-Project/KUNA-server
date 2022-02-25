package konkuk.shop.service;


import konkuk.shop.dto.FindReviewDto;
import konkuk.shop.entity.*;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.form.requestForm.review.AddReviewForm;
import konkuk.shop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final OrderItemRepository orderItemRepository;

    @Value("${image.review}")
    private String reviewPath;

    @Transactional
    public Long saveReview(Long userId, AddReviewForm form) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        Item item = itemRepository.findById(form.getItemId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));

        OrderItem orderItem = orderItemRepository.findById(form.getOrderItemId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ORDER_ITEM));

        if (orderItem.isReviewed()) throw new ApiException(ExceptionEnum.ALREADY_REGISTRY_REVIEW);

        if(form.getRate()<0 || form.getRate()>5) throw new ApiException(ExceptionEnum.ALREADY_REGISTRY_REVIEW);

        Review review = Review.builder()
                .item(item)
                .option(form.getOption())
                .member(member)
                .description(form.getDescription())
                .rate(form.getRate())
                .registryDate(LocalDateTime.now())
                .reviewImages(new ArrayList<>())
                .build();

        List<MultipartFile> reviewImages = form.getReviewImage();
        if (reviewImages != null) {
            try {
                for (MultipartFile reviewImage : reviewImages) {
                    String reviewImageFullName = createStoreFileName(reviewImage.getOriginalFilename());
                    reviewImage.transferTo(new File(reviewPath + reviewImageFullName));
                    ReviewImage saveReviewImage = reviewImageRepository.save(
                            new ReviewImage(reviewImage.getOriginalFilename(), reviewImageFullName, review));
                    review.getReviewImages().add(saveReviewImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new ApiException(ExceptionEnum.FAIL_STORE_IMAGE);
            }
        }
        orderItem.setReviewed(true);
        log.info("리뷰 등록 요청. memberId={}", userId);

        return reviewRepository.save(review).getId();
    }

    private String createStoreFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

    public List<FindReviewDto> findReviewByItemId(Long itemId) {
        log.info("리뷰 목록 요청. itemId={}", itemId);

        return reviewRepository.findAllByItemId(itemId).stream()
                .map(review -> {
                    List<String> reviewImages = review.getReviewImages()
                            .stream().map(ReviewImage::getStore_name)
                            .collect(Collectors.toList());
                    return FindReviewDto.builder()
                            .memberName(review.getMember().getName())
                            .reviewImagesUrl(reviewImages)
                            .description(review.getDescription())
                            .option(review.getOption())
                            .rate(review.getRate())
                            .registryDate(review.getRegistryDate())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
