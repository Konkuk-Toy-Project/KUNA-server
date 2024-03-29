package konkuk.shop.domain.coupon.api;


import konkuk.shop.domain.coupon.dto.RequestAddCouponForm;
import konkuk.shop.domain.coupon.dto.ResponseGetCoupon;
import konkuk.shop.domain.coupon.application.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        String serialNumber = couponService.saveCoupon(form);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("serialNumber", serialNumber);
        return result;
    }

    @PostMapping("/user") // 사용자가 쿠폰 등록
    public ResponseEntity<ResponseGetCoupon> registryCoupon(@AuthenticationPrincipal Long userId,
                                                            @RequestBody HashMap<String, String> map) {
        String serialNumber = map.get("serialNumber");
        ResponseGetCoupon result = couponService.registryCoupon(userId, serialNumber);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping // 로그인 회원이 자신의 쿠폰 확인
    public ResponseEntity<List<ResponseGetCoupon>> getCoupon(@AuthenticationPrincipal Long userId) {
        List<ResponseGetCoupon> result = couponService.getCoupon(userId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
