package konkuk.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class FindOrderDto {
    String address;
    String phone;
    String recipient;
    String deliveryState;
    LocalDateTime orderDate;
    Integer totalPrice;
    Integer usedPoint;
    String payMethod;
    Integer shippingCharge;
    String orderState;
    List<FindOrderItemDto> orderItems;
    Long orderId;
}
