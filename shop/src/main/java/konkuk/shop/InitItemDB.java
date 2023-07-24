package konkuk.shop;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.category.entity.Category;
import konkuk.shop.domain.category.repository.CategoryRepository;
import konkuk.shop.domain.coupon.entity.Coupon;
import konkuk.shop.domain.coupon.entity.CouponKind;
import konkuk.shop.domain.coupon.repository.CouponRepository;
import konkuk.shop.domain.delivery.entity.Delivery;
import konkuk.shop.domain.delivery.entity.DeliveryState;
import konkuk.shop.domain.delivery.repository.DeliveryRepository;
import konkuk.shop.domain.image.entity.DetailImage;
import konkuk.shop.domain.image.entity.ItemImage;
import konkuk.shop.domain.image.entity.Thumbnail;
import konkuk.shop.domain.image.repository.DetailImageRepository;
import konkuk.shop.domain.image.repository.ItemImageRepository;
import konkuk.shop.domain.image.repository.ThumbnailRepository;
import konkuk.shop.domain.item.entity.*;
import konkuk.shop.domain.item.repository.ItemRepository;
import konkuk.shop.domain.item.repository.Option1Repository;
import konkuk.shop.domain.item.repository.Option2Repository;
import konkuk.shop.domain.member.application.MemberFindInfoService;
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
import konkuk.shop.domain.qna.entity.Qna;
import konkuk.shop.domain.qna.repository.QnaRepository;
import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;
import konkuk.shop.domain.member.application.MemberUpdateAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitItemDB {
    private final MemberUpdateAccountService memberUpdateAccountService;
    private final ThumbnailRepository thumbnailRepository;
    private final DetailImageRepository detailImageRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemRepository itemRepository;
    private final Option1Repository option1Repository;
    private final Option2Repository option2Repository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderItemRepository orderItemRepository;
    private final CouponRepository couponRepository;
    private final QnaRepository qnaRepository;
    private final MemberSignupService memberSignupService;
    private final MemberFindInfoService memberFindInfoService;

    @Value("${init.item}")
    private String initItemPath;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() throws IOException {
        log.info("initialize Item database");
        AdminMember adminMember = findAdminMember();
        Category 상의 = categoryRepository.findByName("상의").orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_CATEGORY));
        Category 하의 = categoryRepository.findByName("하의").orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_CATEGORY));
        Category 신발 = categoryRepository.findByName("신발").orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_CATEGORY));
        List<InitItemDto> initItemDtos = parseNote();

        for (InitItemDto initItemDto : initItemDtos) {
            if (initItemDto.getCategory().equals("상의")) initItem(adminMember, 상의, initItemDto);
            else if (initItemDto.getCategory().equals("하의")) initItem(adminMember, 하의, initItemDto);
            else if (initItemDto.getCategory().equals("신발")) initItem(adminMember, 신발, initItemDto);
            else log.info("====init item fail=====");
        }

        Member member = findMember();
        Item item1 = itemRepository.findById(96L).orElseThrow(()-> new ApplicationException(ErrorCode.NO_FIND_ITEM));
        Item item2 = itemRepository.findById(109L).orElseThrow(()-> new ApplicationException(ErrorCode.NO_FIND_ITEM));
        initOrder(member, item1, item2);
        initCoupon(member);
        Member testMember3 = initMember();

        Item item3 = itemRepository.findById(14L).orElseThrow(()-> new ApplicationException(ErrorCode.NO_FIND_ITEM));
        initQna(adminMember, testMember3, item3);

        log.info("====init item Success!=====");
    }

    private AdminMember findAdminMember() {
        Member member = memberRepository.findByEmail("asdf@asdf.com").orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_MEMBER));
        return memberFindInfoService.findAdminByMemberId(member.getId());
    }
    private Member findMember() {
        return memberRepository.findByEmail("asdf2@asdf.com").orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_MEMBER));
    }

    private List<InitItemDto> parseNote() throws IOException {
        Path path = Paths.get(initItemPath);
        List<String> lines = Files.readAllLines(path);
        List<InitItemDto> returnDate = new ArrayList<>();

        int line = 0;

        while (!lines.get(line).equals("===end===")) {
            InitItemDto result = InitItemDto.builder()
                    .name(lines.get(line++))
                    .preference(Integer.parseInt(lines.get(line++)))
                    .sale(Integer.parseInt(lines.get(line++)))
                    .price(Integer.parseInt(lines.get(line++)))
                    .category(lines.get(line++))
                    .optionName(new ArrayList<>())
                    .optionStock(new ArrayList<>())
                    .build();

            line+=1;
//        System.out.println("name: " + lines.get(0));
//        System.out.println("preference: " + Integer.parseInt(lines.get(1)));
//        System.out.println("sale: " + Integer.parseInt(lines.get(2)));
//        System.out.println("price: " + Integer.parseInt(lines.get(3)));
//        System.out.println("category: " + lines.get(4));

            //System.out.println("\n=======thumbnail========");
            while (lines.get(line).length() != 0) {
                //System.out.println(lines.get(line));
                result.setThumbnail(lines.get(line));
                line += 1;
            }

            line += 1;
            //System.out.println("\n=======itemImage========");
            List<String> itemImages = new ArrayList<>();
            while (lines.get(line).length() != 0) {
                //System.out.println(lines.get(line));
                itemImages.add(lines.get(line));
                line += 1;
            }
            result.setItemImages(itemImages);

            line += 1;
            //System.out.println("\n=======detailImage========");
            List<String> detailImages = new ArrayList<>();
            while (lines.get(line).length() != 0) {
                //System.out.println(lines.get(line));
                detailImages.add(lines.get(line));
                line += 1;
            }
            result.setDetailImages(detailImages);

            line += 1;
            //System.out.println("\n=======option========");
            List<String> optionNames = new ArrayList<>();
            List<Integer> optionStocks = new ArrayList<>();
            boolean isOption1 = true;

            int option1Stock = 0;
            for (int i = line; i < lines.size(); i++) {

                if (lines.get(i).equals("===nextItem===")) {
                    line = i + 1;
                    break;
                }
                if (lines.get(i).length() == 0) {
                    isOption1 = true;
                    //System.out.println();
                    List<String> optionName = new ArrayList<>();
                    List<Integer> optionStock = new ArrayList<>();
                    for (int k = 0; k < optionNames.size(); k++) {
                        optionName.add(optionNames.get(k));
                        optionStock.add(optionStocks.get(k));
                    }
                    optionStock.set(0, option1Stock);
                    result.getOptionName().add(optionName);
                    result.getOptionStock().add(optionStock);

                    optionNames.clear();
                    optionStocks.clear();
                    option1Stock = 0;
                    continue;
                }
                if (isOption1) {
                    //System.out.println("option1: " + lines.get(i));
                    optionNames.add(lines.get(i));
                    optionStocks.add(0);
                    isOption1 = false;
                } else {
                    if(lines.get(i).equals("===end===")){
                        line=i;
                        List<String> optionName = new ArrayList<>();
                        List<Integer> optionStock = new ArrayList<>();
                        for (int k = 0; k < optionNames.size(); k++) {
                            optionName.add(optionNames.get(k));
                            optionStock.add(optionStocks.get(k));
                        }
                        optionStock.set(0, option1Stock);
                        result.getOptionName().add(optionName);
                        result.getOptionStock().add(optionStock);
                        break;
                    }
                    String[] newStr = lines.get(i).split("=");
                    //System.out.println("option2: " + newStr[0] + " " + Integer.parseInt(newStr[1]));
                    optionNames.add(newStr[0]);
                    Integer stock=Integer.parseInt(newStr[1]);
                    if(stock==-1) stock = (int)(Math.random()*1000);
                    optionStocks.add(stock);
                    option1Stock += stock;
                }
            }

            returnDate.add(result);
        }
        return returnDate;
    }

    private Item initItem(AdminMember adminMember, Category category, InitItemDto dto) {
        Item item = Item.builder()
                .itemState(ItemState.NORMALITY)
                .adminMember(adminMember)
                .name(dto.getName())
                .preferenceCount(dto.getPreference())
                .registryDate(LocalDateTime.now())
                .version(1) // 첫 번째 버전 : 1
                .sale(dto.getSale())
                .price(dto.getPrice())
                .category(category)
                .itemImages(new ArrayList<>())
                .detailImages(new ArrayList<>())
                .option1s(new ArrayList<>())
                .build();

        item.setThumbnail(thumbnailRepository.save(new Thumbnail(dto.getThumbnail(), dto.getThumbnail(), item)));

        List<String> itemImages = dto.getItemImages();
        for (String itemImage : itemImages) {
            item.getItemImages().add(itemImageRepository.save(new ItemImage(itemImage, itemImage, item)));
        }

        List<String> detailImages = dto.getDetailImages();
        for (String detailImage : detailImages) {
            item.getDetailImages().add(detailImageRepository.save(new DetailImage(detailImage, detailImage, item)));
        }

        item = itemRepository.save(item);

        List<List<String>> optionNames = dto.getOptionName();
        List<List<Integer>> optionStocks = dto.getOptionStock();

        for (int i = 0; i < optionNames.size(); i++) {
            Option1 saveOption1 = option1Repository.save(new Option1(optionNames.get(i).get(0), optionStocks.get(i).get(0)));
            if (optionNames.get(i).size() == 1) continue;

            for (int k = 1; k < optionNames.get(i).size(); k++) {
                Option2 saveOption2 = option2Repository.save(new Option2(optionStocks.get(i).get(k), optionNames.get(i).get(k), saveOption1));
                saveOption1.getOption2s().add(saveOption2);
            }
            saveOption1.setItem(item);
            item.getOption1s().add(saveOption1);
        }
        return item;
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
                .option1("라이트블루")
                .option2("Size L")
                .thumbnailUrl(item.getThumbnail().getStore_name())
                .build();
        orderItems.add(orderItem1);

        int itemPrice2 = Integer.parseInt(String.valueOf(Math.round((100 - item2.getSale()) * 0.01 * item2.getPrice())));
        OrderItem orderItem2 = OrderItem.builder()
                .count(1)
                .isReviewed(false)
                .item(item2)
                .itemName(item2.getName())
                .itemPrice(itemPrice2)
                .itemVersion(item2.getVersion())
                .option1("화이트")
                .option2("Size 160")
                .thumbnailUrl(item2.getThumbnail().getStore_name())
                .build();
        orderItems.add(orderItem2);


        Order order = Order.builder()
                .delivery(delivery)
                .member(member)
                .orderDate(LocalDateTime.now())
                .totalPrice(itemPrice1*2 + itemPrice2) //각각 2개, 1개 주문
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

    private void initCoupon(Member member) {
        Coupon coupon1 = new Coupon(CouponKind.PERCENT, LocalDateTime.now().plusDays(1), "total_price_0", 30, "봄 신상품 런칭 기념");
        coupon1.setUsed(false);
        coupon1.setMember(member);
        coupon1.setSerialNumber("a77sf5e7-a5k1");
        couponRepository.save(coupon1);

        Coupon coupon2 = new Coupon(CouponKind.STATIC, LocalDateTime.now().plusDays(1), "total_price_10000", 10000, "건국 제휴 할인");
        coupon2.setUsed(false);
        coupon2.setMember(member);
        coupon2.setSerialNumber("e7w8cd93-vdf3");
        couponRepository.save(coupon2);
    }

    private Member initMember() {
        SignupDto.Request dto2 = new SignupDto.Request("asdf3@asdf.com", "asdfasdf@3", "testMember3", "01063324829", "19960502", "user");
        Long memberId = memberSignupService.signup(dto2);
        return memberRepository.findById(memberId).orElseThrow(()->new ApplicationException(ErrorCode.NO_FIND_MEMBER));
    }

    private void initQna(AdminMember adminMember, Member member, Item item) {
        Qna qna1 = qnaRepository.save(new Qna(item, member, item.getAdminMember(), "택배 파업 지역도 바로 발송은 되나요? 아니면 파업 종료 이후 순차적으로 발송되나요?", false, "배송 관련 문의."));
        Qna qna2 = qnaRepository.save(new Qna(item, adminMember.getMember(), item.getAdminMember(), "환불 가능한가요.", true, "모델분이 입으신 모습과 제가 입은 모습이 너무 다릅니다. 자괴감들어요ㅜㅜ"));
        Qna qna3 = qnaRepository.save(new Qna(item, member, item.getAdminMember(), "재입고가 되긴 하나요? 구체적인 일정 알 수 있을까요.", false, "재입고 날짜"));

        qna2.registryAnswer("안녕하세요. 저희는 옷을 판매하는 업체입니다. 성형 관련 문의는 병원에 부탁드립니다. 감사합니다.");
        qna3.registryAnswer("안녕하세요.\n" +
                "해당 상품은 2022년 3월 중 재입고 예정입니다.\n" +
                "단, 생산 및 물류 상황, 브랜드의 정책에 따라 달라질 수 있는 점 참고 부탁드립니다.");

    }

}