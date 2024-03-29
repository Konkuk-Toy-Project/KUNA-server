package konkuk.shop.domain.item.api;

import konkuk.shop.domain.item.application.ItemFindService;
import konkuk.shop.domain.item.dto.ItemDetailDto;
import konkuk.shop.domain.item.dto.ItemInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemFindController {
    private final ItemFindService itemFindService;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ItemInfoDto>> findItemListByCategory(@PathVariable Long categoryId) {
        List<ItemInfoDto> response = itemFindService.findItemListByCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDetailDto> findItemDetailByItemId(@PathVariable Long itemId) {
        ItemDetailDto response = itemFindService.findItemById(itemId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ItemInfoDto>> findAllItem() {
        List<ItemInfoDto> response = itemFindService.findAllItem();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/{searchWord}")
    public ResponseEntity<List<ItemInfoDto>> findItemBySearchWord(@PathVariable String searchWord) {
        List<ItemInfoDto> response = itemFindService.findItemBySearchWord(searchWord.toLowerCase());
        return ResponseEntity.ok(response);
    }

}
