package konkuk.shop.controller;

import konkuk.shop.dto.CartItemDto;
import konkuk.shop.form.requestForm.cart.RequestAddItemInCartForm;
import konkuk.shop.form.requestForm.cart.RequestChangeCountForm;
import konkuk.shop.form.responseForm.cart.AddItemInCartResponseForm;
import konkuk.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
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
    public void deleteItemInCart(@PathVariable Long cartItemId) {
        cartService.deleteItem(cartItemId);
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
