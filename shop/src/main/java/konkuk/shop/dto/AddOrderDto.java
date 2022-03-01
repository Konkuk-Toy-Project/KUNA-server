package konkuk.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOrderDto {
    Long orderId;
    Integer totalPrice;
    Integer shippingCharge;
    LocalDateTime orderDate;
    Integer usePoint;
}
