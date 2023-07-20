package konkuk.shop.domain.item.application;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.category.entity.Category;
import konkuk.shop.domain.category.repository.CategoryRepository;
import konkuk.shop.domain.image.entity.DetailImage;
import konkuk.shop.domain.image.entity.ItemImage;
import konkuk.shop.domain.image.entity.Thumbnail;
import konkuk.shop.domain.image.repository.DetailImageRepository;
import konkuk.shop.domain.image.repository.ItemImageRepository;
import konkuk.shop.domain.image.repository.ThumbnailRepository;
import konkuk.shop.domain.item.dto.*;
import konkuk.shop.domain.item.entity.*;
import konkuk.shop.domain.item.repository.ItemRepository;
import konkuk.shop.domain.item.repository.Option1Repository;
import konkuk.shop.domain.item.repository.Option2Repository;
import konkuk.shop.global.error.ApiException;
import konkuk.shop.global.error.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final AdminMemberRepository adminMemberRepository;
    private final Option1Repository option1Repository;
    private final Option2Repository option2Repository;
    private final ItemImageRepository itemImageRepository;
    private final ThumbnailRepository thumbnailRepository;
    private final DetailImageRepository detailImageRepository;
    private final CategoryRepository categoryRepository;

    @Value("${image.thumbnail}")
    private String thumbnailPath;

    @Value("${image.item}")
    private String itemPath;

    @Value("${image.detail}")
    private String detailPath;

    @Transactional
    public Long addItem(Long userId, RequestAddItemDto form) {
        log.info("name={}, price={}, sale={}, categoryId={}", form.getName(), form.getPrice(), form.getSale(), form.getCategoryId());

        AdminMember adminMember = adminMemberRepository.findByMemberId(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ADMIN_MEMBER));
        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CATEGORY));

        Item item = Item.builder()
                .itemState(ItemState.NORMALITY)
                .adminMember(adminMember)
                .name(form.getName())
                .preferenceCount(0)
                .registryDate(LocalDateTime.now())
                .version(1) // 첫 번째 버전 : 1
                .sale(form.getSale())
                .price(form.getPrice())
                .category(category)
                .option1s(new ArrayList<>())
                .itemImages(new ArrayList<>())
                .detailImages(new ArrayList<>())
                .build();

        MultipartFile thumbnail = form.getThumbnail();
        List<MultipartFile> itemImages = form.getItemImages();
        List<MultipartFile> detailImages = form.getDetailImages();
        try {
            if (thumbnail!=null && thumbnail.getSize() != 0) {
                String thumbnailFullName = createStoreFileName(thumbnail.getOriginalFilename());
                thumbnail.transferTo(new File(thumbnailPath + thumbnailFullName));
                Thumbnail saveThumbnail = thumbnailRepository.save(new Thumbnail(thumbnail.getOriginalFilename(), thumbnailFullName, item));
                item.setThumbnail(saveThumbnail);
            }

            for (MultipartFile itemImage : itemImages) {
                if (itemImage.getSize() != 0) {
                    String itemImageFullName = createStoreFileName(itemImage.getOriginalFilename());
                    itemImage.transferTo(new File(itemPath + itemImageFullName));
                    ItemImage saveItemImage = itemImageRepository.save(new ItemImage(itemImage.getOriginalFilename(), itemImageFullName, item));
                    item.getItemImages().add(saveItemImage);
                }
            }

            for (MultipartFile detailImage : detailImages) {
                if (detailImage.getSize() != 0) {
                    String detailImageFullName = createStoreFileName(detailImage.getOriginalFilename());
                    detailImage.transferTo(new File(detailPath + detailImageFullName));
                    DetailImage saveDetailImage = detailImageRepository.save(new DetailImage(detailImage.getOriginalFilename(), detailImageFullName, item));
                    item.getDetailImages().add(saveDetailImage);
                }
            }

            item = itemRepository.save(item);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ExceptionEnum.FAIL_STORE_IMAGE);
        }
        adminMember.getItems().add(item);
        category.getItems().add(item);

        return item.getId();
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


    public List<ResponseMyItem> findItemListByCategory(Long categoryId) {
        log.info("카테고리별 아이템 목록 조회. categoryId={}", categoryId);
        return itemRepository.findByCategoryId(categoryId).stream()
                .map(e -> ResponseMyItem.builder()
                        .itemState(e.getItemState().toString())
                        .name(e.getName())
                        .price(e.getPrice())
                        .sale(e.getSale())
                        .thumbnailUrl(e.getThumbnail().getStore_name())
                        .preference(e.getPreferenceCount())
                        .itemId(e.getId())
                        .categoryId(e.getCategory().getId())
                        .categoryName(e.getCategory().getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveOption(Long userId, List<OptionOneForm> option1s, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));
        if(!item.getAdminMember().getMember().getId().equals(userId))
            throw new ApiException(ExceptionEnum.NO_AUTHORITY_ACCESS_ITEM);

        log.info("item 옵션 추가. itemId={}", itemId);

        for (OptionOneForm option1 : option1s) {
            Option1 saveOption1 = option1Repository.save(new Option1(option1.getName(), option1.getStock()));

            List<OptionTwoForm> option2s = option1.getOption2s();
            for (OptionTwoForm option2 : option2s) {
                Option2 saveOption2 = option2Repository.save(new Option2(option2.getStock(), option2.getName(), saveOption1));
                saveOption1.getOption2s().add(saveOption2);
            }

            saveOption1.setItem(item);
            item.getOption1s().add(saveOption1);
        }
    }

    public ResponseItemDetail findItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));

        log.info("상품 상세 조회. itemId={}", itemId);

        List<String> itemImages = item.getItemImages().stream()
                .map(ItemImage::getStore_name)
                .collect(Collectors.toList());

        List<String> detailImages = item.getDetailImages().stream()
                .map(DetailImage::getStore_name)
                .collect(Collectors.toList());

        List<Option1Dto> option1Dto = item.getOption1s().stream()
                .map(option1 -> new Option1Dto(option1.getName(), option1.getStock(),
                        option1.getId(), makeOption2Dto(option1.getOption2s())))
                .collect(Collectors.toList());


        return ResponseItemDetail.builder()
                .itemId(itemId)
                .itemImageUrl(itemImages)
                .itemState(item.getItemState().toString())
                .categoryName(item.getCategory().getName())
                .DetailImageUrl(detailImages)
                .name(item.getName())
                .sale(item.getSale())
                .preference(item.getPreferenceCount())
                .price(item.getPrice())
                .registryDate(item.getRegistryDate())
                .option1(option1Dto)
                .categoryId(item.getCategory().getId())
                .thumbnailUrl(item.getThumbnail().getStore_name())
                .build();
    }

    private List<Option2Dto> makeOption2Dto(List<Option2> option2s) {
        return option2s.stream()
                .map(option2 -> new Option2Dto(option2.getName(), option2.getStock(), option2.getId()))
                .collect(Collectors.toList());
    }

    public List<ResponseMyItem> findItemBySearchWord(String searchWord) {
        log.info("상품 검색 기능 사용. searchWord={}", searchWord);
        return itemRepository.findAll().stream()
                .filter(e -> e.getName().toLowerCase().contains(searchWord))
                .map(e -> ResponseMyItem.builder()
                        .itemState(e.getItemState().toString())
                        .name(e.getName())
                        .price(e.getPrice())
                        .sale(e.getSale())
                        .thumbnailUrl(e.getThumbnail().getStore_name())
                        .preference(e.getPreferenceCount())
                        .itemId(e.getId())
                        .categoryId(e.getCategory().getId())
                        .categoryName(e.getCategory().getName())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public List<ResponseMyItem> findItemByUserId(Long userId) {
        AdminMember adminMember = adminMemberRepository.findByMemberId(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ADMIN_MEMBER));

        log.info("아이템 조회 요청. memberId={}, adminMemberId={}", userId, adminMember.getId());

        return itemRepository.findByAdminMember(adminMember).stream()
                .map(e -> ResponseMyItem.builder()
                        .itemState(e.getItemState().toString())
                        .name(e.getName())
                        .price(e.getPrice())
                        .sale(e.getSale())
                        .thumbnailUrl(e.getThumbnail().getStore_name())
                        .preference(e.getPreferenceCount())
                        .itemId(e.getId())
                        .categoryId(e.getCategory().getId())
                        .categoryName(e.getCategory().getName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ResponseMyItem> findAllItem() {
        log.info("모든 상품 조회");
        return itemRepository.findAll().stream()
                .map(e -> ResponseMyItem.builder()
                        .itemState(e.getItemState().toString())
                        .name(e.getName())
                        .price(e.getPrice())
                        .sale(e.getSale())
                        .thumbnailUrl(e.getThumbnail().getStore_name())
                        .preference(e.getPreferenceCount())
                        .itemId(e.getId())
                        .categoryId(e.getCategory().getId())
                        .categoryName(e.getCategory().getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void editPriceByItemId(Long userId, Long itemId, Integer price, Integer sale) {
        AdminMember adminMember = adminMemberRepository.findByMemberId(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ADMIN_MEMBER));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));

        if (!adminMember.getId().equals(item.getAdminMember().getId()))
            throw new ApiException(ExceptionEnum.NO_AUTHORITY_ACCESS_ITEM);

        log.info("상품의 가격, 세일 수정 요청. itemId={}, 가격={}->{}, 세일={}->{}",
                itemId, item.getPrice(), price, item.getSale(), sale);

        item.changePriceAndSale(price, sale);
    }
}
