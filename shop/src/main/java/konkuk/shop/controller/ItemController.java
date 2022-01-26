package konkuk.shop.controller;


import konkuk.shop.dto.AddItemDto;
import konkuk.shop.entity.AdminMember;
import konkuk.shop.entity.Category;
import konkuk.shop.entity.Item;
import konkuk.shop.entity.Option1;
import konkuk.shop.form.requestForm.item.RequestAddItem;
import konkuk.shop.form.requestForm.item.RequestAddOptionForm;
import konkuk.shop.form.responseForm.item.ResponseItemList;
import konkuk.shop.service.CategoryService;
import konkuk.shop.service.ItemService;
import konkuk.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;
    private final MemberService memberService;
    private final CategoryService categoryService;

    @PostMapping()
    public HashMap<String, Object> registryItem(@AuthenticationPrincipal Long userId,
                                                RequestAddItem form) {
        log.info("name={}, price={}, sale={}, categoryId={}", form.getName(), form.getPrice(), form.getSale(), form.getCategoryId());
        log.info("detailImages size={}, itemImage size={}", form.getDetailImages().size(), form.getItemImages().size());
        AdminMember adminMember = memberService.findAdminById(userId);
        Category category = categoryService.findCategoryById(form.getCategoryId());


        AddItemDto addItemDto = AddItemDto.builder()
                .itemName(form.getName())
                .price(form.getPrice())
                .sale(form.getSale()).adminMember(adminMember)
                .category(category)
                .thumbnail(form.getThumbnail())
                .detailImage(form.getDetailImages())
                .itemImage(form.getItemImages())
                .build();

        Item item = itemService.addItem(addItemDto);
        adminMember.getItems().add(item);
        category.getItems().add(item);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("itemId", item.getId());
        return result;
    }

    @PostMapping("/{itemId}/option")
    public void registryOption(@RequestBody RequestAddOptionForm form, @PathVariable Long itemId) {
       itemService.saveOption(form.getOption1s(), itemId);
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<ResponseItemList>> findItemListByCategory(@PathVariable String category) {
        List<Item> items = itemService.findItemListByCategory(category);
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

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
