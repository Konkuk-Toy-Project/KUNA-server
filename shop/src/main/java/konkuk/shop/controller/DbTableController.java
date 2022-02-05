package konkuk.shop.controller;

import konkuk.shop.entity.*;
import konkuk.shop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DbTableController {
    private final AdminMemberRepository adminMemberRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CategoryRepository categoryRepository;
    private final CouponRepository couponRepository;
    private final DeliveryRepository deliveryRepository;
    private final ItemRepository itemRepository;
    private final Option1Repository option1Repository;
    private final Option2Repository option2Repository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PreferenceRepository preferenceRepository;
    private final QnaRepository qnaRepository;
    private final ReviewRepository reviewRepository;
    private final ItemImageRepository itemImageRepository;
    private final DetailImageRepository detailImageRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ThumbnailRepository thumbnailRepository;

    @GetMapping("/table")
    public String showDbTable(Model model) {
        log.info("DB 테이블 보기");
        List<AdminMember> adminMembers = adminMemberRepository.findAll();
        List<Member> members = memberRepository.findAll();
        List<CartItem> cartItems = cartRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        List<Coupon> coupons = couponRepository.findAll();
        List<Delivery> deliveries = deliveryRepository.findAll();
        List<Item> items = itemRepository.findAll();
        List<Option1> option1s = option1Repository.findAll();
        List<Option2> option2s = option2Repository.findAll();
        List<Order> orders = orderRepository.findAll();
        List<OrderItem> orderItems = orderItemRepository.findAll();
        List<PreferenceItem> preferenceItems = preferenceRepository.findAll();
        List<Qna> qnas = qnaRepository.findAll();
        List<Review> reviews = reviewRepository.findAll();
        List<ItemImage> itemImages = itemImageRepository.findAll();
        List<DetailImage> detailImages = detailImageRepository.findAll();
        List<ReviewImage> reviewImages = reviewImageRepository.findAll();
        List<Thumbnail> thumbnails = thumbnailRepository.findAll();


        model.addAttribute("adminMembers", adminMembers);
        model.addAttribute("members", members);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("categories", categories);
        model.addAttribute("coupons", coupons);
        model.addAttribute("deliveries", deliveries);
        model.addAttribute("items", items);
        model.addAttribute("option1s", option1s);
        model.addAttribute("option2s", option2s);
        model.addAttribute("orders", orders);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("preferenceItems", preferenceItems);
        model.addAttribute("qnas", qnas);
        model.addAttribute("reviews", reviews);
        model.addAttribute("itemImages", itemImages);
        model.addAttribute("detailImages", detailImages);
        model.addAttribute("reviewImages", reviewImages);
        model.addAttribute("thumbnails", thumbnails);
        return "showDbTable";
    }
}
