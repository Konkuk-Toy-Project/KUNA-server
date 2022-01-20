package konkuk.shop.service;

import konkuk.shop.dto.AddItemDto;
import konkuk.shop.entity.Item;
import konkuk.shop.entity.Thumbnail;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    // thumbnail, detailImage, itemImage 은 이 메소드에서 저장
    // adminMemberId, CategoryItemId, option1Id 은 Id만 전달 받음
    public Item addItem(AddItemDto dto) {

        /**
         * 계속.. 이어서... 나가줘..
         */
        MultipartFile thumbnail = dto.getThumbnail();
        String thumbnailFullName = createStoreFileName(thumbnail.getOriginalFilename());
        try{
            thumbnail.transferTo(new File(thumbnailPath + thumbnailFullName));
            thumbnailRepository.save(new Thumbnail(thumbnail.getOriginalFilename(), thumbnailFullName));
        } catch (Exception e){
            e.printStackTrace();
            throw new ApiException(ExceptionEnum.FAIL_STORE_THUMBNAIL));
        }

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
