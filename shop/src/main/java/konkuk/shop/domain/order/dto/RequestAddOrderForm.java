package konkuk.shop.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddOrderForm {
    String address;
    String recipient;
    String phone;
    String payMethod;
    Integer usePoint;
    Integer totalPrice; // 검증용
    Integer shippingCharge;
    Long couponId;
    List<OrderItemForm> orderItems;
}
