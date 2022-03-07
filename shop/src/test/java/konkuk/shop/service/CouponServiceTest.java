package konkuk.shop.service;

import konkuk.shop.entity.Coupon;
import konkuk.shop.entity.CouponKind;
import konkuk.shop.entity.Member;
import konkuk.shop.form.requestForm.coupon.RequestAddCouponForm;
import konkuk.shop.form.responseForm.coupon.ResponseGetCoupon;
import konkuk.shop.repository.CouponRepository;
import konkuk.shop.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.UpperCase;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class CouponServiceTest {
    private final String kind = "percent";
    private final Integer rate = 50;
    private final String expiredDate = LocalDateTime.now().plusMinutes(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    private final String condition = "5000";
    private final String name = "회원가입 감사 쿠폰";
    private final String serial=UUID.randomUUID().toString().substring(0, 13);
    @Mock
    CouponRepository couponRepository;
    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    CouponService couponService;
    private Coupon coupon;

    @BeforeEach
    void dataSetting() {
        coupon = new Coupon(CouponKind.PERCENT, LocalDateTime.now().plusMinutes(30),
                condition, rate, name, false, serial);
    }

    @Test
    @DisplayName("새로운 쿠폰 생성 테스트")
    void saveCoupon() {
        //given
        RequestAddCouponForm form = new RequestAddCouponForm(kind, rate, expiredDate, condition, name);
        given(couponRepository.save(any(Coupon.class))).willReturn(coupon);

        //when
        String serial = couponService.saveCoupon(form);

        //then
        assertThat(serial).hasSize(13);
        verify(couponRepository).save(any(Coupon.class));
    }

    @Test
    @DisplayName("가지고 있는 쿠폰 조회 테스트")
    void getCoupon() {
        //given
        Member member = new Member();
        member.getCoupons().add(coupon);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        //when
        List<ResponseGetCoupon> result = couponService.getCoupon(1L);
        ResponseGetCoupon resultCoupon = result.get(0);
        //then
        assertThat(resultCoupon.getCouponId()).isEqualTo(coupon.getId());
        assertThat(resultCoupon.getCouponKind()).isEqualTo(kind.toUpperCase());
        assertThat(resultCoupon.getCouponCondition()).isEqualTo(condition);
        assertThat(resultCoupon.getExpiredDate()).isEqualTo(coupon.getExpiredDate());
        assertThat(resultCoupon.getName()).isEqualTo(name);
        assertThat(resultCoupon.getIsUsed()).isEqualTo(false);
        assertThat(resultCoupon.getRate()).isEqualTo(rate);
        verify(memberRepository).findById(1L);
    }

    @Test
    @DisplayName("쿠폰 등록 테스트")
    void registryCoupon() {
        //given
        Member member = new Member();
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(couponRepository.findBySerialNumber(serial)).willReturn(Optional.of(coupon));

        //when
        ResponseGetCoupon result = couponService.registryCoupon(1L, serial);

        //then
        assertThat(result.getCouponId()).isEqualTo(coupon.getId());
        assertThat(result.getCouponKind()).isEqualTo(kind.toUpperCase());
        assertThat(result.getCouponCondition()).isEqualTo(condition);
        assertThat(result.getExpiredDate()).isEqualTo(coupon.getExpiredDate());
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getIsUsed()).isEqualTo(false);
        assertThat(result.getRate()).isEqualTo(rate);
        verify(memberRepository).findById(1L);
    }
}