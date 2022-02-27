package konkuk.shop.form.requestForm.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddCouponForm {
    String kind;
    Integer rate;
    String expiredDate; // 2021-11-05 13:47:13.248
    String condition;
    String name;
}
