package konkuk.shop.service;

import konkuk.shop.dto.AddItemDto;
import konkuk.shop.entity.*;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
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

    @Value("${image.thumbnail}")
    private String thumbnailPath;

    @Value("${image.item}")
    private String itemPath;

    @Value("${image.detail}")
    private String detailPath;

    // thumbnail, detailImage, itemImage 은 이 메소드에서 저장
    // adminMember, CategoryItem, option1 은 객체를 전달 받음
    public Item addItem(AddItemDto dto) {
        Item item = Item.builder()
                .itemState(ItemState.NORMALITY)
                .adminMember(dto.getAdminMember())
                .name(dto.getItemName())
                .preferenceCount(0)
                .registryDate(LocalDateTime.now())
                .version(1) // 첫 번째 버전 : 1
                .sale(0) // 첫 등록시 세일 정책 : 0%
                .build();

        MultipartFile thumbnail = dto.getThumbnail();
        List<MultipartFile> itemImages = dto.getItemImage();
        List<MultipartFile> detailImages = dto.getDetailImage();
        try{
            String thumbnailFullName = createStoreFileName(thumbnail.getOriginalFilename());
            thumbnail.transferTo(new File(thumbnailPath + thumbnailFullName));
            Thumbnail saveThumbnail = thumbnailRepository.save(new Thumbnail(thumbnail.getOriginalFilename(), thumbnailFullName, item));
            item.setThumbnail(saveThumbnail);

            for(MultipartFile itemImage:itemImages){
                String itemImageFullName = createStoreFileName(itemImage.getOriginalFilename());
                itemImage.transferTo(new File(itemPath + itemImageFullName));
                ItemImage saveItemImage = itemImageRepository.save(new ItemImage(itemImage.getOriginalFilename(), itemImageFullName, item));
                item.getItemImages().add(saveItemImage);
            }

            for(MultipartFile detailImage:detailImages){
                String detailImageFullName = createStoreFileName(detailImage.getOriginalFilename());
                detailImage.transferTo(new File(detailPath + detailImageFullName));
                DetailImage saveDetailImage = detailImageRepository.save(new DetailImage(detailImage.getOriginalFilename(), detailImageFullName, item));
                item.getDetailImages().add(saveDetailImage);
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new ApiException(ExceptionEnum.FAIL_STORE_IMAGE);
        }

        return itemRepository.save(item);
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


}
