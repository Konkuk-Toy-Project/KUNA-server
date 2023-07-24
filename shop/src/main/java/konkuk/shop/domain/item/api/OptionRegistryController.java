package konkuk.shop.domain.item.api;

import konkuk.shop.domain.item.application.ItemService;
import konkuk.shop.domain.item.dto.OptionAddDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class OptionRegistryController {
    private final ItemService itemService;

    @PostMapping("/{itemId}/option")
    public void registryOption(@AuthenticationPrincipal Long userId, @PathVariable Long itemId,
                               @RequestBody OptionAddDto form) {
        itemService.saveOption(userId, form.getOption1s(), itemId);
    }
}
