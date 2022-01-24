package konkuk.shop.service;

import konkuk.shop.entity.Coupon;
import konkuk.shop.entity.CouponKind;
import konkuk.shop.entity.Member;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.form.requestForm.coupon.RequestAddCouponForm;
import konkuk.shop.repository.CouponRepository;
import konkuk.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    public Coupon saveCoupon(RequestAddCouponForm form) {
        CouponKind couponKind = convertCouponKind(form.getKind());
        LocalDateTime expiredDate = convertExpiredDate(form.getExpiredDate());

        Coupon coupon = new Coupon(couponKind, expiredDate, form.getCondition(), form.getRate(), form.getName());
        coupon.setUsed(false);
        coupon.setSerialNumber(UUID.randomUUID().toString().substring(0, 13));
        return couponRepository.save(coupon);
    }

    private CouponKind convertCouponKind(String kind) {
        if (kind.equals("percent")) return CouponKind.PERCENT;
        else if (kind.equals("static")) return CouponKind.STATIC;
        return null; // 오류
    }

    private LocalDateTime convertExpiredDate(String expiredDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return LocalDateTime.parse(expiredDate, formatter);
    }

    public List<Coupon> getCoupon(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        List<Coupon> coupons = member.getCoupons();
        return coupons;
    }

    public void registryCoupon(Long userId, String serialNumber) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        Coupon coupon = couponRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_COUPON));

        coupon.setMember(member);
        member.getCoupons().add(coupon);

        couponRepository.save(coupon); //업데이트
    }
}
