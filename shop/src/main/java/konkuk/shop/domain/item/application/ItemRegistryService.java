package konkuk.shop.domain.item.application;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.admin.exception.AdminNotFoundException;
import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.category.entity.Category;
import konkuk.shop.domain.category.exception.CategoryNotFoundException;
import konkuk.shop.domain.category.repository.CategoryRepository;
import konkuk.shop.domain.image.entity.DetailImage;
import konkuk.shop.domain.image.entity.ItemImage;
import konkuk.shop.domain.image.entity.Thumbnail;
import konkuk.shop.domain.image.exception.FailStoreImageException;
import konkuk.shop.domain.image.repository.DetailImageRepository;
import konkuk.shop.domain.image.repository.ItemImageRepository;
import konkuk.shop.domain.image.repository.ThumbnailRepository;
import konkuk.shop.domain.item.dto.ItemAddDto;
import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemRegistryService {
    private final ItemRepository itemRepository;
    private final AdminMemberRepository adminMemberRepository;
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
    public Long addItem(Long userId, ItemAddDto.Request form) {
        AdminMember adminMember = adminMemberRepository.findByMemberId(userId)
                .orElseThrow(AdminNotFoundException::new);
        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        Item item = Item.initialItemToRegistry(form.getName(), form.getPrice(), form.getSale(), category, adminMember);

        try {
            setThumbnail(item, form.getThumbnail());
            setItemImages(item, form.getItemImages());
            setDetailImages(item, form.getDetailImages());
        } catch (IOException e) {
            e.printStackTrace();
            throw new FailStoreImageException();
        }

        item = itemRepository.save(item);
        adminMember.getItems().add(item);
        category.getItems().add(item);

        return item.getId();
    }

    private void setDetailImages(Item item, List<MultipartFile> detailImages) throws IOException {
        for (MultipartFile detailImage : detailImages) {
            if (detailImage.getSize() != 0) {
                String detailImageFullName = storeFile(detailImage, detailPath);
                DetailImage saveDetailImage = detailImageRepository.save(new DetailImage(detailImage.getOriginalFilename(), detailImageFullName, item));
                item.getDetailImages().add(saveDetailImage);
            }
        }
    }

    private void setItemImages(Item item, List<MultipartFile> itemImages) throws IOException {
        for (MultipartFile itemImage : itemImages) {
            if (itemImage.getSize() != 0) {
                String itemImageFullName = storeFile(itemImage, itemPath);
                ItemImage saveItemImage = itemImageRepository.save(new ItemImage(itemImage.getOriginalFilename(), itemImageFullName, item));
                item.getItemImages().add(saveItemImage);
            }
        }
    }

    private void setThumbnail(Item item, MultipartFile thumbnail) throws IOException {
        if (thumbnail != null && thumbnail.getSize() != 0) {
            String thumbnailFullName = storeFile(thumbnail, thumbnailPath);
            Thumbnail saveThumbnail = thumbnailRepository.save(new Thumbnail(thumbnail.getOriginalFilename(), thumbnailFullName, item));
            item.setThumbnail(saveThumbnail);
        }
    }

    private String storeFile(MultipartFile multipartFile, String path) throws IOException {
        String imageFullName = createStoreFileName(multipartFile.getOriginalFilename());
        File file = new File(path + imageFullName);
        multipartFile.transferTo(file);
        return imageFullName;
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
