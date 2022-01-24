package konkuk.shop.controller;


import konkuk.shop.entity.Coupon;
import konkuk.shop.form.requestForm.coupon.RequestAddCouponForm;
import konkuk.shop.form.responseForm.coupon.ResponseGetCoupon;
import konkuk.shop.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {
    private final CouponService couponService;

    @PostMapping // 관리자가 쿠폰 등록
    public HashMap<String, Object> addCoupon(@RequestBody RequestAddCouponForm form) {
        Coupon coupon = couponService.saveCoupon(form);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("serialNumber", coupon.getSerialNumber());
        return result;
    }

    @PostMapping("/user") // 사용자가 쿠폰 등록
    public void registryCoupon(@AuthenticationPrincipal Long userId,
                               @RequestBody HashMap<String, String> map) {
        String serialNumber = map.get("serialNumber");
        couponService.registryCoupon(userId, serialNumber);
    }

    @GetMapping // 로그인 회원이 자신의 쿠폰 확인
    public ResponseEntity<List<ResponseGetCoupon>> getCoupon(@AuthenticationPrincipal Long userId) {
        List<Coupon> coupons = couponService.getCoupon(userId);
        List<ResponseGetCoupon> result = new ArrayList<>();

        for (Coupon coupon : coupons) {
            result.add(new ResponseGetCoupon(coupon.getCouponKind().toString(), coupon.getRate(),
                    coupon.getExpiredDate(), coupon.getCouponCondition(), coupon.getName()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
