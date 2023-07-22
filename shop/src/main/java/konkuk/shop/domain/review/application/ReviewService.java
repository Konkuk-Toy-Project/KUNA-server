package konkuk.shop.domain.review.application;


import konkuk.shop.domain.image.entity.ReviewImage;
import konkuk.shop.domain.image.repository.ReviewImageRepository;
import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.item.repository.ItemRepository;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.domain.order.entity.OrderItem;
import konkuk.shop.domain.order.repository.OrderItemRepository;
import konkuk.shop.domain.review.entity.Review;
import konkuk.shop.domain.review.repository.ReviewRepository;
import konkuk.shop.dto.FindReviewDto;
import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;
import konkuk.shop.domain.review.dto.AddReviewForm;
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
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_MEMBER));
        Item item = itemRepository.findById(form.getItemId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_ITEM_BY_ID));

        OrderItem orderItem = orderItemRepository.findById(form.getOrderItemId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_ORDER_ITEM));

        if (orderItem.isReviewed()) throw new ApplicationException(ErrorCode.ALREADY_REGISTRY_REVIEW);

        if(form.getRate()<0 || form.getRate()>5) throw new ApplicationException(ErrorCode.ALREADY_REGISTRY_REVIEW);

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
                throw new ApplicationException(ErrorCode.FAIL_STORE_IMAGE);
            }
        }
        orderItem.setReviewed(true);
        if(reviewImages==null) member.changePoint((int) (orderItem.getItemPrice()*orderItem.getCount()*0.01));
        else member.changePoint((int) (orderItem.getItemPrice()*orderItem.getCount()*0.03));
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
