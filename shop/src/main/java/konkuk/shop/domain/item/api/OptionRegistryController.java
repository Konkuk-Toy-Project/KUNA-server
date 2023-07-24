package konkuk.shop.domain.item.api;

import konkuk.shop.domain.item.application.OptionRegistryService;
import konkuk.shop.domain.item.dto.OptionAddDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class OptionRegistryController {
    private final OptionRegistryService optionRegistryService;

    @PostMapping("/{itemId}/option")
    public void registryOption(@AuthenticationPrincipal Long userId, @PathVariable Long itemId,
                               @RequestBody OptionAddDto form) {
        optionRegistryService.saveOption(userId, form.getOption1s(), itemId);
    }
}
