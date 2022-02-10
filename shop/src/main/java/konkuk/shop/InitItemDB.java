package konkuk.shop;

import konkuk.shop.entity.*;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.repository.*;
import konkuk.shop.service.MemberService;
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
    private final MemberService memberService;
    private final ThumbnailRepository thumbnailRepository;
    private final DetailImageRepository detailImageRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemRepository itemRepository;
    private final Option1Repository option1Repository;
    private final Option2Repository option2Repository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    @Value("${init.item}")
    private String initItemPath;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() throws IOException {
        log.info("initialize Item database");
        AdminMember adminMember = findAdminMember();
        Category 상의 = categoryRepository.findByName("상의").orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CATEGORY));
        Category 하의 = categoryRepository.findByName("하의").orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CATEGORY));
        Category 신발 = categoryRepository.findByName("신발").orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CATEGORY));
        List<InitItemDto> initItemDtos = parseNote();

        for (InitItemDto initItemDto : initItemDtos) {
            if (initItemDto.getCategory().equals("상의")) initItem(adminMember, 상의, initItemDto);
            else if (initItemDto.getCategory().equals("하의")) initItem(adminMember, 하의, initItemDto);
            else if (initItemDto.getCategory().equals("신발")) initItem(adminMember, 신발, initItemDto);
            else log.info("====init item fail=====");
        }
    }

    private AdminMember findAdminMember() {
        Member member = memberRepository.findByEmail("asdf@asdf.com").orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        return memberService.findAdminByMemberId(member.getId());
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
                    optionStocks.add(Integer.parseInt(newStr[1]));
                    option1Stock += Integer.parseInt(newStr[1]);
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

}