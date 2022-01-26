package konkuk.shop.form.requestForm.order;

import lombok.Data;

import java.util.List;

@Data
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
