package konkuk.shop;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.cart.application.CartService;
import konkuk.shop.domain.category.application.CategoryService;
import konkuk.shop.domain.category.entity.Category;
import konkuk.shop.domain.coupon.entity.Coupon;
import konkuk.shop.domain.coupon.entity.CouponKind;
import konkuk.shop.domain.coupon.repository.CouponRepository;
import konkuk.shop.domain.delivery.entity.Delivery;
import konkuk.shop.domain.delivery.entity.DeliveryState;
import konkuk.shop.domain.delivery.repository.DeliveryRepository;
import konkuk.shop.domain.image.entity.DetailImage;
import konkuk.shop.domain.image.entity.ItemImage;
import konkuk.shop.domain.image.entity.ReviewImage;
import konkuk.shop.domain.image.entity.Thumbnail;
import konkuk.shop.domain.image.repository.DetailImageRepository;
import konkuk.shop.domain.image.repository.ItemImageRepository;
import konkuk.shop.domain.image.repository.ThumbnailRepository;
import konkuk.shop.domain.item.application.ItemRegistryService;
import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.item.entity.ItemState;
import konkuk.shop.domain.item.entity.Option1;
import konkuk.shop.domain.item.entity.Option2;
import konkuk.shop.domain.item.repository.ItemRepository;
import konkuk.shop.domain.item.repository.Option1Repository;
import konkuk.shop.domain.item.repository.Option2Repository;
import konkuk.shop.domain.member.application.MemberFindInfoService;
import konkuk.shop.domain.member.application.MemberUpdateAccountService;
import konkuk.shop.domain.member.application.MemberSignupService;
import konkuk.shop.domain.member.dto.SignupDto;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.domain.order.entity.Order;
import konkuk.shop.domain.order.entity.OrderItem;
import konkuk.shop.domain.order.entity.OrderState;
import konkuk.shop.domain.order.entity.PayMethod;
import konkuk.shop.domain.order.repository.OrderItemRepository;
import konkuk.shop.domain.order.repository.OrderRepository;
import konkuk.shop.domain.preference.application.PreferenceService;
import konkuk.shop.domain.qna.entity.Qna;
import konkuk.shop.domain.qna.repository.QnaRepository;
import konkuk.shop.domain.review.entity.Review;
import konkuk.shop.domain.review.repository.ReviewRepository;
import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final MemberUpdateAccountService memberUpdateAccountService;
    private final CategoryService categoryService;
    private final ItemRegistryService itemRegistryService;
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
    private final QnaRepository qnaRepository;
    private final MemberSignupService memberSignupService;
    private final MemberFindInfoService memberFindInfoService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        log.info("initialize database");
        AdminMember adminMember = initAdminMember();
        Member member = initMember();
        member.changePoint(500);
        Category category = initCategory();
        Item item = initItem(adminMember, category);
        Item item2 = initItem2(adminMember);
        initCart(adminMember.getMember().getId(), item);
        initCoupon(adminMember.getMember());
        Order order = initOrder(adminMember.getMember(), item, item2);
        Order order2 = initOrder(member, item, item2);

        initReview(adminMember.getMember(), order, 0);
        initReview(member, order2, 0);
        initReview(member, order2, 1);
        Long preferenceId = initPreference(adminMember.getMember(), item);
        initQna(member, item);
    }

    private AdminMember initAdminMember() {
        SignupDto.Request dto1 = new SignupDto.Request("asdf@asdf.com", "asdfasdf@1", "testMember1", "01012345678", "20000327", "admin");
        Long saveMemberId = memberSignupService.signup(dto1);

        return memberFindInfoService.findAdminByMemberId(saveMemberId);
    }

    private Member initMember() {
        SignupDto.Request dto2 = new SignupDto.Request("asdf2@asdf.com", "asdfasdf@2", "testMember2", "01087654321", "19991003", "user");
        Long memberId = memberSignupService.signup(dto2);
        return memberRepository.findById(memberId).orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_MEMBER));
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
                .name("플립 오프숄더 골지 카라 니트")
                .preferenceCount(4)
                .registryDate(LocalDateTime.now())
                .version(1) // 첫 번째 버전 : 1
                .sale(22)
                .price(25000)
                .category(category)
                .itemImages(new ArrayList<>())
                .detailImages(new ArrayList<>())
                .option1s(new ArrayList<>())
                .build();

        item.setThumbnail(thumbnailRepository.save(new Thumbnail("thumbnail1.webp", "thumbnail1.webp", item)));

        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage1-1", "itemImage1-1.jpeg", item)));
        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage1-2", "itemImage1-2.jpeg", item)));
        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage1-3", "itemImage1-3.jpeg", item)));

        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage1-1", "detailImage1-1.jpeg", item)));
        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage1-2", "detailImage1-2.jpeg", item)));
        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage1-3", "detailImage1-3.png", item)));

        item = itemRepository.save(item);

        Option1 saveOption1 = option1Repository.save(new Option1("블랙", 134));
        Option2 saveOption2 = option2Repository.save(new Option2(134, "ONE Size", saveOption1));
        saveOption1.getOption2s().add(saveOption2);
        saveOption1.setItem(item);
        item.getOption1s().add(saveOption1);

        Option1 saveOption21 = option1Repository.save(new Option1("스카이블루", 31));
        Option2 saveOption22 = option2Repository.save(new Option2(31, "ONE Size", saveOption21));
        saveOption21.getOption2s().add(saveOption22);
        saveOption21.setItem(item);
        item.getOption1s().add(saveOption21);

        Option1 saveOption31 = option1Repository.save(new Option1("아이보리", 345));
        Option2 saveOption32 = option2Repository.save(new Option2(345, "ONE Size", saveOption31));
        saveOption31.getOption2s().add(saveOption32);
        saveOption31.setItem(item);
        item.getOption1s().add(saveOption31);

        Option1 saveOption41 = option1Repository.save(new Option1("그린", 32));
        Option2 saveOption42 = option2Repository.save(new Option2(32, "ONE Size", saveOption41));
        saveOption41.getOption2s().add(saveOption42);
        saveOption41.setItem(item);
        item.getOption1s().add(saveOption41);

        return item;
    }

    private Item initItem2(AdminMember adminMember) {
        Category category = categoryService.findCategoryById(6L); // 신발
        Item item = Item.builder()
                .itemState(ItemState.NORMALITY)
                .adminMember(adminMember)
                .name("라운드쿠션스니커즈")
                .preferenceCount(89)
                .registryDate(LocalDateTime.now())
                .version(1) // 첫 번째 버전 : 1
                .sale(25)
                .price(26000)
                .category(category)
                .itemImages(new ArrayList<>())
                .detailImages(new ArrayList<>())
                .option1s(new ArrayList<>())
                .build();

        item.setThumbnail(thumbnailRepository.save(new Thumbnail("thumbnail12.webp", "thumbnail12.webp", item)));

        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage12-1", "itemImage12-1.jpeg", item)));
        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage12-2", "itemImage12-2.jpeg", item)));
        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage12-3", "itemImage12-3.jpeg", item)));
        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage12-4", "itemImage12-4.jpeg", item)));
        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage12-5", "itemImage12-5.jpeg", item)));

        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage12-1", "detailImage12-1.jpeg", item)));
        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage12-2", "detailImage12-2.jpeg", item)));
        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage12-3", "detailImage12-3.jpeg", item)));
        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage12-4", "detailImage12-4.jpeg", item)));
        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage12-5", "detailImage12-5.jpeg", item)));
        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage12-6", "detailImage12-6.jpeg", item)));

        item = itemRepository.save(item);

        Option1 saveOption1 = option1Repository.save(new Option1("블랙", 1083));
        saveOption1.getOption2s().add(option2Repository.save(new Option2(134, "Size 230", saveOption1)));
        saveOption1.getOption2s().add(option2Repository.save(new Option2(342, "Size 235", saveOption1)));
        saveOption1.getOption2s().add(option2Repository.save(new Option2(532, "Size 240", saveOption1)));
        saveOption1.getOption2s().add(option2Repository.save(new Option2(54, "Size 245", saveOption1)));
        saveOption1.getOption2s().add(option2Repository.save(new Option2(21, "Size 250", saveOption1)));
        saveOption1.setItem(item);
        item.getOption1s().add(saveOption1);

        Option1 saveOption21 = option1Repository.save(new Option1("화이트", 235));
        saveOption21.getOption2s().add(option2Repository.save(new Option2(13, "Size 230", saveOption1)));
        saveOption21.getOption2s().add(option2Repository.save(new Option2(56, "Size 235", saveOption1)));
        saveOption21.getOption2s().add(option2Repository.save(new Option2(43, "Size 240", saveOption1)));
        saveOption21.getOption2s().add(option2Repository.save(new Option2(78, "Size 245", saveOption1)));
        saveOption21.getOption2s().add(option2Repository.save(new Option2(45, "Size 250", saveOption1)));
        saveOption21.setItem(item);
        item.getOption1s().add(saveOption21);

        return item;
    }

    private void initCart(Long memberId, Item item) {
        cartService.addItem(memberId, item.getId(), item.getOption1s().get(0).getId(), item.getOption1s().get(0).getOption2s().get(0).getId(), 2);
    }

    private void initCoupon(Member member) {
        Coupon coupon1 = new Coupon(CouponKind.STATIC, LocalDateTime.now().plusDays(1), "total_price_5000", 1000, "회원가입 감사쿠폰");
        coupon1.setUsed(false);
        coupon1.setMember(member);
        coupon1.setSerialNumber("a1b2c3d4-e5f8");
        couponRepository.save(coupon1);

        Coupon coupon2 = new Coupon(CouponKind.PERCENT, LocalDateTime.now().plusDays(1), "total_price_5000", 50, "관리자가 사랑하는만큼 쿠폰");
        coupon2.setUsed(false);
        coupon2.setMember(member);
        coupon2.setSerialNumber("a1b2c3d4-e5f7");
        couponRepository.save(coupon2);

        Coupon coupon3 = new Coupon(CouponKind.STATIC, LocalDateTime.now().plusDays(1), "total_price_30000", 5000, "런칭 기념 쿠폰");
        coupon3.setUsed(false);
        coupon3.setSerialNumber("a1b2c3d4-e5f6");
        couponRepository.save(coupon3);
    }

    private void initReview(Member member, Order order, int orderItemOrder) {
        OrderItem orderItem = order.getOrderItems().get(orderItemOrder);

        Review review = Review.builder()
                .item(orderItem.getItem())
                .option(orderItem.getOption1() + "/" + orderItem.getOption2())
                .member(member)
                .description("생각보다 사이즈가 크긴 한데, 입었을 때 진짜 편하네요. 운동복으로 딱 좋아요!")
                .rate(4)
                .registryDate(LocalDateTime.now())
                .reviewImages(new ArrayList<>())
                .build();
        Review saveReview = reviewRepository.save(review);

        saveReview.getReviewImages().add(new ReviewImage("reviewImage1-1", "reviewImage1-1.png", saveReview));
        saveReview.getReviewImages().add(new ReviewImage("reviewImage1-2", "reviewImage1-2.png", saveReview));
        orderItem.setReviewed(true);
    }

    private Long initPreference(Member member, Item item) {
        return preferenceService.savePreferenceItem(member.getId(), item.getId());
    }

    private Order initOrder(Member member, Item item, Item item2) {
        Delivery delivery = deliveryRepository.save(
                new Delivery("서울특별시 ~ ", "010123456789", "이진용", DeliveryState.PREPARING));

        List<OrderItem> orderItems = new ArrayList<>();

        int itemPrice1 = Integer.parseInt(String.valueOf(Math.round((100 - item.getSale()) * 0.01 * item.getPrice())));
        OrderItem orderItem1 = OrderItem.builder()
                .count(3)
                .isReviewed(false)
                .item(item)
                .itemName(item.getName())
                .itemPrice(itemPrice1)
                .itemVersion(item.getVersion())
                .option1("그린")
                .option2("ONE Size")
                .thumbnailUrl(item.getThumbnail().getStore_name())
                .build();
        orderItems.add(orderItem1); // 58500

        int itemPrice2 = Integer.parseInt(String.valueOf(Math.round((100 - item2.getSale()) * 0.01 * item2.getPrice())));
        OrderItem orderItem2 = OrderItem.builder()
                .count(1)
                .isReviewed(false)
                .item(item2)
                .itemName(item2.getName())
                .itemPrice(itemPrice2)
                .itemVersion(item2.getVersion())
                .option1("블랙")
                .option2("Size 240")
                .thumbnailUrl(item2.getThumbnail().getStore_name())
                .build();
        orderItems.add(orderItem2);


        Order order = Order.builder()
                .delivery(delivery)
                .member(member)
                .orderDate(LocalDateTime.now())
                .totalPrice(itemPrice1 * 3 + itemPrice2) //각각 3개, 1개 주문
                .coupon(null)
                .usedPoint(0)
                .payMethod(PayMethod.CARD)
                .shippingCharge(0)
                .orderState(OrderState.NORMALITY)
                .orderItems(new ArrayList<>())
                .orderItems(orderItems)
                .build();

        Order saveOrder = orderRepository.save(order);

        orderItem1.setOrder(saveOrder);
        orderItemRepository.save(orderItem1);
        orderItem2.setOrder(saveOrder);
        orderItemRepository.save(orderItem2);

        return saveOrder;
    }

    private void initQna(Member member, Item item) {
        Qna qna1 = new Qna(item, member, item.getAdminMember(), "1년전에 주문했는데, 혹시 환불 가능한가요? ㅜㅜ", true, "환불 부탁드립니다.");
        Qna qna2 = new Qna(item, member, item.getAdminMember(), "265사이즈를 주문했는데 사이즈 260 교환요청합니다.\n" +
                "빠른처리부탁드립니다", false, "교환 요청드립니다.");
        qna2.registryAnswer("안녕하세요.\n" +
                "문의하신 상품 마이페이지를 통해 직접 접수해 주신 부분으로 확인됩니다.\n" +
                "상품이 반송지로 도착하면, 검수 완료 이후(영업일 기준 약 2~3일 소요) 교환 처리가 이뤄집니다.");
        qnaRepository.save(qna1);
        qnaRepository.save(qna2);
    }
}