package konkuk.shop.controller;

import konkuk.shop.entity.Member;
import konkuk.shop.form.requestForm.order.RequestAddOrderForm;
import konkuk.shop.service.ItemService;
import konkuk.shop.service.MemberService;
import konkuk.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @PostMapping
    public void addOrder(@AuthenticationPrincipal Long userId,
                         @RequestBody RequestAddOrderForm form) {
        orderService.addOrder(userId, form);


    }
}
