package konkuk.shop.controller;


import konkuk.shop.dto.AddItemDto;
import konkuk.shop.entity.AdminMember;
import konkuk.shop.entity.Category;
import konkuk.shop.entity.Item;
import konkuk.shop.form.requestForm.item.RequestAddItem;
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

import java.util.ArrayList;
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
    public void registryItem(@AuthenticationPrincipal Long userId,
                             @RequestBody RequestAddItem form) {
        AdminMember adminMember = memberService.findAdminById(userId);
        Category category = categoryService.findCategoryById(form.getCategoryId());

        AddItemDto addItemDto = AddItemDto.builder()
                .itemName(form.getName())
                .price(form.getPrice())
                .sale(form.getSale()).adminMember(adminMember)
                .category(category)
                .thumbnail(form.getThumbnail())
                .detailImage(form.getDetailImage())
                .itemImage(form.getItemImages())
                //.option1()
                .build();

        itemService.addItem(addItemDto);
    }


    @GetMapping("/{category}")
    public ResponseEntity<List<ResponseItemList>> findItemListByCategory(@PathVariable String category) {
        List<Item> items = itemService.findItemListByCategory(category);
        List<ResponseItemList> result = new ArrayList<>();

        /**
         * 아마 지연 로딩때문에 썸네일 부분에서 오류날듯?
         */
        items.forEach(e -> {
            ResponseItemList item = ResponseItemList.builder()
                    .itemState(e.getItemState().toString())
                    .name(e.getName())
                    .price(e.getPrice())
                    .sale(e.getSale())
                    .thumbnailUrl(e.getThumbnail().getStore_name())
                    .preferenceCount(e.getPreferenceCount())
                    .build();
            result.add(item);
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
