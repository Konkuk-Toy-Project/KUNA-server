package konkuk.shop.controller;

import konkuk.shop.form.requestForm.cart.RequestAddItemInCartForm;
import konkuk.shop.service.CartService;
import konkuk.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final Environment env;


    @PostMapping("/item")
    public void addItemInCart(@AuthenticationPrincipal Long userId,
                              @RequestBody RequestAddItemInCartForm form) {
        cartService.addItem(userId, form.getItemId(), form.getOption1Id(), form.getOption2Id(), form.getCount());
    }

}
