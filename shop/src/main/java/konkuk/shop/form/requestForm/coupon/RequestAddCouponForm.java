package konkuk.shop.form.requestForm.coupon;

import lombok.Data;

@Data
public class RequestAddCouponForm {
    String kind;
    Integer rate;
    String expiredDate; // 2021-11-05 13:47:13.248
    String condition;
    String name;
}
