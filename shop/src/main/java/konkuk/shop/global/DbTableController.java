package konkuk.shop.global;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.cart.entity.CartItem;
import konkuk.shop.domain.cart.repository.CartRepository;
import konkuk.shop.domain.category.entity.Category;
import konkuk.shop.domain.category.repository.CategoryRepository;
import konkuk.shop.domain.coupon.entity.Coupon;
import konkuk.shop.domain.coupon.repository.CouponRepository;
import konkuk.shop.domain.delivery.entity.Delivery;
import konkuk.shop.domain.delivery.repository.DeliveryRepository;
import konkuk.shop.domain.image.entity.DetailImage;
import konkuk.shop.domain.image.entity.ItemImage;
import konkuk.shop.domain.image.entity.ReviewImage;
import konkuk.shop.domain.image.entity.Thumbnail;
import konkuk.shop.domain.image.repository.DetailImageRepository;
import konkuk.shop.domain.image.repository.ItemImageRepository;
import konkuk.shop.domain.image.repository.ReviewImageRepository;
import konkuk.shop.domain.image.repository.ThumbnailRepository;
import konkuk.shop.domain.item.entity.*;
import konkuk.shop.domain.item.repository.ItemRepository;
import konkuk.shop.domain.item.repository.Option1Repository;
import konkuk.shop.domain.item.repository.Option2Repository;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.domain.order.entity.Order;
import konkuk.shop.domain.order.entity.OrderItem;
import konkuk.shop.domain.order.repository.OrderItemRepository;
import konkuk.shop.domain.order.repository.OrderRepository;
import konkuk.shop.domain.preference.entity.PreferenceItem;
import konkuk.shop.domain.preference.repository.PreferenceRepository;
import konkuk.shop.domain.qna.entity.Qna;
import konkuk.shop.domain.qna.repository.QnaRepository;
import konkuk.shop.domain.review.entity.Review;
import konkuk.shop.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
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

        List<String> table = Arrays.asList("adminMember", "member", "cartItem", "category", "coupon", "delivery",
                "item", "option1", "option2", "order", "orderItem", "preferenceItem", "qna", "review", "itemImage",
                "detailImage", "reviewImage", "thumbnail");
        model.addAttribute("tableName", table);

        return "showDbTable";
    }
}
