package konkuk.shop.domain.item.application;

import konkuk.shop.domain.image.entity.DetailImage;
import konkuk.shop.domain.image.entity.ItemImage;
import konkuk.shop.domain.item.dto.ItemDetailDto;
import konkuk.shop.domain.item.dto.ItemInfoDto;
import konkuk.shop.domain.item.dto.Option1Dto;
import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.item.exception.ItemNotFoundException;
import konkuk.shop.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemFindService {
    private final ItemRepository itemRepository;

    public List<ItemInfoDto> findItemListByCategory(Long categoryId) {
        return itemRepository.findByCategoryId(categoryId).stream()
                .map(ItemInfoDto::of)
                .toList();
    }

    public ItemDetailDto findItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFoundException::new);

        List<String> itemImages = getItemImages(item);
        List<String> detailImages = getDetailImages(item);
        List<Option1Dto> option1Dto = getOptions(item);

        return ItemDetailDto.builder()
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

    private List<Option1Dto> getOptions(Item item) {
        return item.getOption1s().stream()
                .map(Option1Dto::of)
                .toList();
    }

    private List<String> getDetailImages(Item item) {
        return item.getDetailImages().stream()
                .map(DetailImage::getStore_name)
                .toList();
    }

    private List<String> getItemImages(Item item) {
        return item.getItemImages().stream()
                .map(ItemImage::getStore_name)
                .toList();
    }

    public List<ItemInfoDto> findItemBySearchWord(String searchWord) {
        return itemRepository.findAll().stream()
                .filter(item -> item.getName().toLowerCase().contains(searchWord))
                .map(ItemInfoDto::of)
                .toList();
    }

    public List<ItemInfoDto> findAllItem() {
        return itemRepository.findAll().stream()
                .map(ItemInfoDto::of)
                .toList();
    }
}
