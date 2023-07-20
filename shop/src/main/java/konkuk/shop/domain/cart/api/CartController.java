package konkuk.shop.domain.cart.api;

import konkuk.shop.dto.CartItemDto;
import konkuk.shop.domain.cart.dto.RequestAddItemInCartForm;
import konkuk.shop.domain.cart.dto.RequestChangeCountForm;
import konkuk.shop.domain.cart.dto.AddItemInCartResponseForm;
import konkuk.shop.domain.cart.application.CartService;
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
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<List<AddItemInCartResponseForm>> addItemInCart(@AuthenticationPrincipal Long userId,
                                                                         @RequestBody List<RequestAddItemInCartForm> forms) {
        List<AddItemInCartResponseForm> result = new ArrayList<>();
        for(RequestAddItemInCartForm form : forms) {
            Long cartItemId = cartService.addItem(userId, form.getItemId(), form.getOption1Id(), form.getOption2Id(), form.getCount());
            result.add(new AddItemInCartResponseForm(cartItemId));
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{cartItemId}")
    public void deleteItemInCart(@AuthenticationPrincipal Long userId, @PathVariable Long cartItemId) {
        cartService.deleteItemInCart(userId, cartItemId);
    }

    @GetMapping
    public ResponseEntity<List<CartItemDto>> findAllByUserIdInCart(@AuthenticationPrincipal Long userId) {
        List<CartItemDto> result = cartService.findAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/{cartItemId}")
    public void changeCount(@AuthenticationPrincipal Long userId, @RequestBody RequestChangeCountForm form,
                            @PathVariable Long cartItemId) {
        cartService.changeCount(userId, cartItemId, form.getCount());
    }

}
