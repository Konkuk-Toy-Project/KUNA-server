package konkuk.shop.domain.item.api;

import konkuk.shop.domain.item.application.ItemService;
import konkuk.shop.domain.item.dto.ItemAddDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemRegistryController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemAddDto.Response> registryItem(@AuthenticationPrincipal Long userId,
                                                            ItemAddDto.Request form) {
        Long itemId = itemService.addItem(userId, form);
        ItemAddDto.Response response = new ItemAddDto.Response(itemId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
