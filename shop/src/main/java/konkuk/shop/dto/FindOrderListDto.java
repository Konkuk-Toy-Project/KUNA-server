package konkuk.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FindOrderListDto {
    LocalDateTime orderDate;
    Integer totalPrice;
    Long orderId;
    String orderState;
    String deliveryState;
}
