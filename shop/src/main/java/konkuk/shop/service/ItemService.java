package konkuk.shop.service;

import konkuk.shop.dto.AddItemDto;
import konkuk.shop.entity.*;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.form.requestForm.item.*;
import konkuk.shop.form.requestForm.qna.RequestAddQnaForm;
import konkuk.shop.form.responseForm.item.ResponseItemList;
import konkuk.shop.repository.*;
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
import java.util.Locale;
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

    // thumbnail, detailImage, itemImage 은 이 메소드에서 저장
    // adminMember, CategoryItem, option1 은 객체를 전달 받음
    @Transactional
    public Item addItem(AddItemDto dto) {
        Item item = Item.builder()
                .itemState(ItemState.NORMALITY)
                .adminMember(dto.getAdminMember())
                .name(dto.getItemName())
                .preferenceCount(0)
                .registryDate(LocalDateTime.now())
                .version(1) // 첫 번째 버전 : 1
                .sale(dto.getSale())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .option1s(new ArrayList<>())
                .itemImages(new ArrayList<>())
                .detailImages(new ArrayList<>())
                .build();

        MultipartFile thumbnail = dto.getThumbnail();
        List<MultipartFile> itemImages = dto.getItemImage();
        List<MultipartFile> detailImages = dto.getDetailImage();
        try {
            String thumbnailFullName = createStoreFileName(thumbnail.getOriginalFilename());
            thumbnail.transferTo(new File(thumbnailPath + thumbnailFullName));
            Thumbnail saveThumbnail = thumbnailRepository.save(new Thumbnail(thumbnail.getOriginalFilename(), thumbnailFullName, item));
            item.setThumbnail(saveThumbnail);

            for (MultipartFile itemImage : itemImages) {
                String itemImageFullName = createStoreFileName(itemImage.getOriginalFilename());
                itemImage.transferTo(new File(itemPath + itemImageFullName));
                ItemImage saveItemImage = itemImageRepository.save(new ItemImage(itemImage.getOriginalFilename(), itemImageFullName, item));
                item.getItemImages().add(saveItemImage);
            }

            for (MultipartFile detailImage : detailImages) {
                String detailImageFullName = createStoreFileName(detailImage.getOriginalFilename());
                detailImage.transferTo(new File(detailPath + detailImageFullName));
                DetailImage saveDetailImage = detailImageRepository.save(new DetailImage(detailImage.getOriginalFilename(), detailImageFullName, item));
                item.getDetailImages().add(saveDetailImage);
            }

            item = itemRepository.save(item);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ExceptionEnum.FAIL_STORE_IMAGE);
        }

        return item;
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


    public List<ResponseItemList> findItemListByCategory(String categoryName) {
        List<Item> items = new ArrayList<>();

        if (categoryName.equals("all")) items = itemRepository.findAll();
        else {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CATEGORY));
            items = itemRepository.findByCategory(category);
        }

        List<ResponseItemList> result = new ArrayList<>();
        items.forEach(e -> {
            ResponseItemList item = ResponseItemList.builder()
                    .itemState(e.getItemState().toString())
                    .name(e.getName())
                    .price(e.getPrice())
                    .sale(e.getSale())
                    .thumbnailUrl(e.getThumbnail().getStore_name())
                    .preferenceCount(e.getPreferenceCount())
                    .itemId(e.getId())
                    .build();
            result.add(item);
        });
        return result;
    }

    @Transactional
    public void saveOption(List<OptionOneForm> option1s, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));

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

    public ResponseItemDetail findItemById(Long itemId){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));

        List<String> itemImages = item.getItemImages().stream()
                .map(e -> e.getStore_name())
                .collect(Collectors.toList());

        List<String> detailImages = item.getDetailImages().stream()
                .map(e -> e.getStore_name())
                .collect(Collectors.toList());

        List<Option1Dto> option1Dto = new ArrayList<>();

        List<Option1> option1s = item.getOption1s();
        for (Option1 option1 : option1s) {
            List<Option2Dto> option2Dto = new ArrayList<>();

            List<Option2> option2s = option1.getOption2s();
            for (Option2 option2 : option2s) {
                option2Dto.add(new Option2Dto(option2.getName(), option2.getStock(), option2.getId()));
            }
            option1Dto.add(new Option1Dto(option1.getName(), option1.getStock(), option1.getId(), option2Dto));
        }


        return ResponseItemDetail.builder()
                .itemId(itemId)
                .itemImageUrl(itemImages)
                .itemState(item.getItemState().toString())
                .category(item.getCategory().getName())
                .DetailImageUrl(detailImages)
                .name(item.getName())
                .sale(item.getSale())
                .preferenceCount(item.getPreferenceCount())
                .price(item.getPrice())
                .registryDate(item.getRegistryDate())
                .option1(option1Dto)
                .build();
    }

    public List<ResponseItemList> findItemBySearchWord(String searchWord) {
        /**
         * 검색 쿼리에 대한 정확성 및 정책 강화 필요
         */
        List<Item> items = itemRepository.findAll();

        List<ResponseItemList> result = new ArrayList<>();

        items.forEach(e -> {
            String itemName = e.getName();
            if(itemName.toLowerCase().contains(searchWord)){
                ResponseItemList item = ResponseItemList.builder()
                        .itemState(e.getItemState().toString())
                        .name(e.getName())
                        .price(e.getPrice())
                        .sale(e.getSale())
                        .thumbnailUrl(e.getThumbnail().getStore_name())
                        .preferenceCount(e.getPreferenceCount())
                        .itemId(e.getId())
                        .build();
                result.add(item);
            }
        });
        return result;
    }
}
