package konkuk.shop.controller;

import konkuk.shop.dto.AddOrderDto;
import konkuk.shop.dto.FindOrderDto;
import konkuk.shop.entity.Member;
import konkuk.shop.form.requestForm.order.RequestAddOrderForm;
import konkuk.shop.form.responseForm.member.ResponseSignupForm;
import konkuk.shop.service.ItemService;
import konkuk.shop.service.MemberService;
import konkuk.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<AddOrderDto> addOrder(@AuthenticationPrincipal Long userId,
                                                       @RequestBody RequestAddOrderForm form) {
        AddOrderDto result = orderService.addOrder(userId, form);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<FindOrderDto> getOrderDetail(@PathVariable Long orderId) {
        /**
         * 로그인 본인이 주문한건지 확인하는 로직 필요
         */
        FindOrderDto result = orderService.findOrderDetailList(orderId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
