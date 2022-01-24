package konkuk.shop.service;

import konkuk.shop.dto.AddItemDto;
import konkuk.shop.entity.*;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.form.requestForm.item.OptionOneForm;
import konkuk.shop.form.requestForm.item.OptionTwoForm;
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
import java.util.UUID;

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


    public List<Item> findItemListByCategory(String categoryName) {
        if (categoryName.equals("all")) return itemRepository.findAll();

        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CATEGORY));

        return itemRepository.findByCategory(category);
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
}
