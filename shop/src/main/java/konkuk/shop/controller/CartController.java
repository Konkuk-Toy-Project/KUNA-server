package konkuk.shop.controller;

import konkuk.shop.dto.CartItemDto;
import konkuk.shop.form.requestForm.cart.RequestAddItemInCartForm;
import konkuk.shop.form.requestForm.cart.RequestChangeCountForm;
import konkuk.shop.form.requestForm.cart.RequestDeleteItemInCartForm;
import konkuk.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final Environment env;


    @PostMapping
    public void addItemInCart(@AuthenticationPrincipal Long userId,
                              @RequestBody RequestAddItemInCartForm form) {
        cartService.addItem(userId, form.getItemId(), form.getOption1Id(), form.getOption2Id(), form.getCount());
    }

    @DeleteMapping
    public void deleteItemInCart(@RequestBody RequestDeleteItemInCartForm form) {
        cartService.deleteItem(form.getCartItemId());
    }

    @GetMapping
    public ResponseEntity<List<CartItemDto>> findAllByUserIdInCart(@AuthenticationPrincipal Long userId) {
        List<CartItemDto> result = cartService.findAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/count")
    public void changeCount(@AuthenticationPrincipal Long userId, @RequestBody RequestChangeCountForm form) {
        cartService.changeCount(userId, form.getCartItemId(), form.getCount());
    }

}
