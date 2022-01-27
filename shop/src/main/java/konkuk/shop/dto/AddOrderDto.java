package konkuk.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AddOrderDto {
    Long orderId;
    Integer totalPrice;
    LocalDateTime orderDate;
}
