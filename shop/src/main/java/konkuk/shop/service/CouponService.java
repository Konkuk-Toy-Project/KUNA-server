package konkuk.shop.service;

import konkuk.shop.entity.Coupon;
import konkuk.shop.entity.CouponKind;
import konkuk.shop.entity.Member;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.form.requestForm.coupon.RequestAddCouponForm;
import konkuk.shop.form.responseForm.coupon.ResponseGetCoupon;
import konkuk.shop.repository.CouponRepository;
import konkuk.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    public Coupon saveCoupon(RequestAddCouponForm form) {
        CouponKind couponKind = convertCouponKind(form.getKind());
        LocalDateTime expiredDate = convertExpiredDate(form.getExpiredDate());

        Coupon coupon = new Coupon(couponKind, expiredDate, form.getCondition(), form.getRate(),
                form.getName(), false, makeSerialNumber());

        log.info("관리자가 쿠폰 등록. serialNumber={}", coupon.getSerialNumber());
        return couponRepository.save(coupon);
    }

    private String makeSerialNumber() {
        return UUID.randomUUID().toString().substring(0, 13);
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

    public List<ResponseGetCoupon> getCoupon(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        log.info("쿠폰 목록 조회 userId={}", userId);

        return member.getCoupons().stream()
                .map(e -> new ResponseGetCoupon(e.getCouponKind().toString(), e.getRate(),
                        e.getExpiredDate(), e.getCouponCondition(), e.getName(), e.isUsed(), e.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseGetCoupon registryCoupon(Long userId, String serialNumber) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        Coupon coupon = couponRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_COUPON));

        if (coupon.getMember() != null) throw new ApiException(ExceptionEnum.ALREADY_REGISTRY_COUPON);
        if (coupon.getExpiredDate().isBefore(LocalDateTime.now()))
            throw new ApiException(ExceptionEnum.EXPIRED_COUPON);

        log.info("사용자가 쿠폰 등록. couponId={}", coupon.getId());

        coupon.setMember(member);
        member.getCoupons().add(coupon);

        return new ResponseGetCoupon(coupon.getCouponKind().toString(), coupon.getRate(),
                coupon.getExpiredDate(), coupon.getCouponCondition(), coupon.getName(), coupon.isUsed(), coupon.getId());
    }
}
