package konkuk.shop.controller;


import konkuk.shop.form.requestForm.item.RequestAddItemDto;
import konkuk.shop.form.requestForm.item.RequestAddOptionForm;
import konkuk.shop.form.requestForm.item.ResponseItemDetail;
import konkuk.shop.form.responseForm.item.ResponseMyItem;
import konkuk.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<?> registryItem(@AuthenticationPrincipal Long userId,
                                          RequestAddItemDto form) {
        Long itemId = itemService.addItem(userId, form);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("itemId", itemId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/{itemId}/option")
    public void registryOption(@AuthenticationPrincipal Long userId, @PathVariable Long itemId,
                               @RequestBody RequestAddOptionForm form) {
        itemService.saveOption(userId, form.getOption1s(), itemId);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ResponseMyItem>> findItemListByCategory(@PathVariable Long categoryId) {
        List<ResponseMyItem> result = itemService.findItemListByCategory(categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ResponseItemDetail> findItemDetailByItemId(@PathVariable Long itemId) {
        ResponseItemDetail result = itemService.findItemById(itemId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping
    public ResponseEntity<List<ResponseMyItem>> findAllItem() {
        List<ResponseMyItem> result = itemService.findAllItem();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/search/{searchWord}")
    public ResponseEntity<List<ResponseMyItem>> findItemBySearchWord(@PathVariable String searchWord) {
        List<ResponseMyItem> result = itemService.findItemBySearchWord(searchWord.toLowerCase());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
