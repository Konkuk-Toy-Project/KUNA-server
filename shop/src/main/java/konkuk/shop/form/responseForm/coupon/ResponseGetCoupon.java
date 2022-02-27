package konkuk.shop.form.responseForm.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGetCoupon {
    String couponKind;
    Integer rate;
    LocalDateTime expiredDate;
    String couponCondition;
    String name;
    Boolean isUsed;
    Long couponId;
}
