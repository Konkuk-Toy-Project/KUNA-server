package konkuk.shop;

import konkuk.shop.entity.*;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.repository.*;
import konkuk.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitItemDB {
    private final MemberService memberService;
    private final ThumbnailRepository thumbnailRepository;
    private final DetailImageRepository detailImageRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemRepository itemRepository;
    private final Option1Repository option1Repository;
    private final Option2Repository option2Repository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        log.info("initialize Item database");
        AdminMember adminMember = findAdminMember();
        Category 상의 = categoryRepository.findByName("상의").orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CATEGORY));
        Category 하의 = categoryRepository.findByName("하의").orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CATEGORY));
        Category 신발 = categoryRepository.findByName("신발").orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CATEGORY));
    }

    private AdminMember findAdminMember() {
        Member member = memberRepository.findByEmail("asdf@asdf.com").orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        return memberService.findAdminByMemberId(member.getId());
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

        item.setThumbnail(thumbnailRepository.save(new Thumbnail("thumbnail1.webp", "thumbnail1.webp", item)));

        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage1-1", "itemImage1-1.jpeg", item)));
        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage1-2", "itemImage1-2.jpeg", item)));
        item.getItemImages().add(itemImageRepository.save(new ItemImage("itemImage1-3", "itemImage1-3.jpeg", item)));

        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage1-1", "detailImage1-1.jpeg", item)));
        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage1-2", "detailImage1-2.jpeg", item)));
        item.getDetailImages().add(detailImageRepository.save(new DetailImage("detailImage1-3", "detailImage1-3.jpeg", item)));

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

}