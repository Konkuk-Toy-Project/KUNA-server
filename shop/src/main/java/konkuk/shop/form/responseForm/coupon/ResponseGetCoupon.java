package konkuk.shop.form.responseForm.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResponseGetCoupon {
    String couponKind;
    Integer rate;
    LocalDateTime expiredDate;
    String couponCondition;
    String name;
}
