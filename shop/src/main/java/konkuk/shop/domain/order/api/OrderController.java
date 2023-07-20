package konkuk.shop.domain.order.api;

import konkuk.shop.dto.AddOrderDto;
import konkuk.shop.dto.FindOrderDto;
import konkuk.shop.dto.FindOrderListDto;
import konkuk.shop.dto.OrderItemDto;
import konkuk.shop.domain.order.dto.RequestAddOrderForm;
import konkuk.shop.domain.order.application.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<AddOrderDto> addOrder(@AuthenticationPrincipal Long userId,
                                                @RequestBody RequestAddOrderForm form) {
        AddOrderDto result = orderService.addOrder(userId, form);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<FindOrderDto> getOrderDetail(@AuthenticationPrincipal Long userId,
                                                       @PathVariable Long orderId) {
        FindOrderDto result = orderService.findOrderDetailList(userId, orderId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping
    public ResponseEntity<List<FindOrderListDto>> getOrderList(@AuthenticationPrincipal Long userId) {
        List<FindOrderListDto> result = orderService.findOrderList(userId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    //  주문한 제품명, 리뷰 작성 유무, 자신의 주문 상품 Id
    @GetMapping("/item")
    public ResponseEntity<List<OrderItemDto>> getOrderItems(@AuthenticationPrincipal Long userId){
        List<OrderItemDto> result = orderService.findOrderItemList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
