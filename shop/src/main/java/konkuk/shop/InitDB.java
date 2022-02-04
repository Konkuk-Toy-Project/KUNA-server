package konkuk.shop;

import konkuk.shop.dto.SignupDto;
import konkuk.shop.entity.*;
import konkuk.shop.repository.*;
import konkuk.shop.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitDB {
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final ThumbnailRepository thumbnailRepository;
    private final DetailImageRepository detailImageRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemRepository itemRepository;
    private final Option1Repository option1Repository;
    private final Option2Repository option2Repository;
    private final CartService cartService;
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final PreferenceService preferenceService;
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Value("${image.thumbnail}")
    private String thumbnailPath;

    @Value("${image.item}")
    private String itemPath;

    @Value("${image.detail}")
    private String detailPath;

    @Value("${image.review}")
    private String reviewPath;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        log.info("initialize database");
        AdminMember adminMember = initAdminMember();
        Member member = initMember();
        Category category = initCategory();
        Item item = initItem(adminMember, category);
        initCart(adminMember.getMember().getId(), item);
        initCoupon();
        initReview(member, item);
        Long preferenceId = initPreference(member, item);
        Order order = initOrder(member, item);
    }

    private AdminMember initAdminMember() {
        SignupDto dto1 = new SignupDto("asdf@asdf.com", "asdfasdf@1", "testMember1", "01012345678", "20000327", "admin");
        Long saveMemberId = memberService.signup(dto1);
        return memberService.findAdminByMemberId(saveMemberId);
    }

    private Member initMember() {
        SignupDto dto2 = new SignupDto("asdf2@asdf.com", "asdfasdf@2", "testMember2", "01087654321", "19991003", "user");
        Long member = memberService.signup(dto2);
        return memberRepository.findById(member).get();
    }

    private Category initCategory() {
        Category category = categoryService.addCategory("상의");
        categoryService.addCategory("하의");
        categoryService.addCategory("신발");
        return category;
    }

    private Item initItem(AdminMember adminMember, Category category) {
        Item item = Item.builder()
                .itemState(ItemState.NORMALITY)
                .adminMember(adminMember)
                .name("Jordan 1 Retro High - Men Shoes")
                .preferenceCount(0)
                .registryDate(LocalDateTime.now())
                .version(1) // 첫 번째 버전 : 1
                .sale(0)
                .price(199000)
                .category(category)
                .itemImages(new ArrayList<>())
                .detailImages(new ArrayList<>())
                .option1s(new ArrayList<>())
                .build();

        item.setThumbnail(thumbnailRepository.save(new Thumbnail("thumbnail.webp", "thumbnail.webp", item)));

        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage1", "itemImage1.jpeg", item)));
        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage2", "itemImage2.jpeg", item)));
        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage3", "itemImage3.jpeg", item)));

        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage1", "detailImage1.jpeg", item)));
        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage2", "detailImage2.jpeg", item)));
        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage3", "detailImage3.jpeg", item)));

        item = itemRepository.save(item);

        Option1 saveOption1 = option1Repository.save(new Option1("Size M", 26));
        Option2 saveOption2 = option2Repository.save(new Option2(13, "파란색", saveOption1));
        saveOption1.getOption2s().add(saveOption2);
        saveOption2 = option2Repository.save(new Option2(13, "노란색", saveOption1));
        saveOption1.getOption2s().add(saveOption2);
        saveOption1.setItem(item);
        item.getOption1s().add(saveOption1);

        saveOption1 = option1Repository.save(new Option1("Size L", 6));
        saveOption2 = option2Repository.save(new Option2(3, "파란색", saveOption1));
        saveOption1.getOption2s().add(saveOption2);
        saveOption2 = option2Repository.save(new Option2(3, "노란색", saveOption1));
        saveOption1.getOption2s().add(saveOption2);
        saveOption1.setItem(item);
        item.getOption1s().add(saveOption1);

        return item;
    }

    private void initCart(Long memberId, Item item) {
        cartService.addItem(memberId, item.getId(), item.getOption1s().get(0).getId(), item.getOption1s().get(0).getOption2s().get(0).getId(), 2);
    }

    private void initCoupon() {
        Coupon coupon = new Coupon(CouponKind.STATIC, LocalDateTime.now().plusDays(1), "total_price_5000", 1000, "회원가입 감사쿠폰");
        coupon.setUsed(false);
        coupon.setSerialNumber("a1b2c3d4-e5f6");
        couponRepository.save(coupon);
    }

    private void initReview(Member member, Item item) {
        Review review = Review.builder()
                .item(item)
                .option("임시 옵션 데이터1/옵션 데이터2")
                .member(member)
                .description("생각보다 사이즈가 크긴 한데, 입었을 때 진짜 편하네요. 운동복으로 딱 좋아요!")
                .rate(4)
                .registryDate(LocalDateTime.now())
                .reviewImages(new ArrayList<>())
                .build();
        Review saveReview = reviewRepository.save(review);

        saveReview.getReviewImages().add(new ReviewImage("reviewImage1", "reviewImage1.png", saveReview));
        saveReview.getReviewImages().add(new ReviewImage("reviewImage2", "reviewImage2.png", saveReview));
    }

    private Long initPreference(Member member, Item item) {
        return preferenceService.savePreferenceItem(member.getId(), item.getId());
    }

    private Order initOrder(Member member, Item item) {
        Delivery delivery = deliveryRepository.save(
                new Delivery("서울특별시 ~ ", "010123456789", "이진용", DeliveryState.PREPARING));

        List<OrderItem> orderItems = new ArrayList<>();

        OrderItem orderItem = OrderItem.builder()
                .count(3)
                .isReviewed(false)
                .item(item)
                .itemName(item.getName())
                .itemPrice(item.getPrice())
                .itemVersion(item.getVersion())
                .option1("option1")
                .option2("option2")
                .thumbnailUrl("thumbnail.webp")
                .build();
        orderItems.add(orderItem);

        Order order = Order.builder()
                .delivery(delivery)
                .member(member)
                .orderDate(LocalDateTime.now())
                .totalPrice(50000)
                .coupon(null)
                .usedPoint(0)
                .payMethod(PayMethod.CARD)
                .shippingCharge(5000)
                .orderState(OrderState.NORMALITY)
                .orderItems(new ArrayList<>())
                .orderItems(orderItems)
                .build();

        Order saveOrder = orderRepository.save(order);

        orderItem.setOrder(saveOrder);
        orderItemRepository.save(orderItem);

        return saveOrder;
    }
}