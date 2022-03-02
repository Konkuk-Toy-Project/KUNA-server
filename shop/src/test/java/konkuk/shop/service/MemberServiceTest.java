package konkuk.shop.service;

import konkuk.shop.dto.FindMemberInfoByUserIdDto;
import konkuk.shop.dto.LoginDto;
import konkuk.shop.dto.SignupDto;
import konkuk.shop.entity.AdminMember;
import konkuk.shop.entity.Member;
import konkuk.shop.repository.AdminMemberRepository;
import konkuk.shop.repository.MemberRepository;
import konkuk.shop.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class MemberServiceTest {
    private final String name = "testMember";
    private final String email = "asdf@asdf.com";
    private final String password = "asdfasdf@1";
    private final String phone = "01012345678";
    private final String birth = "20000327";
    private final String role = "user";
    private final Long memberId = 3L;
    private final String token = "JWTToken";
    @Mock
    MemberRepository memberRepository;
    @Mock
    AdminMemberRepository adminMemberRepository;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    TokenProvider tokenProvider;
    @InjectMocks
    MemberService memberService;

    @Test
    @DisplayName("이메일 중복 테스트")
    void isDuplicateEmail() {
        //given
        given(memberRepository.existsByEmail(email)).willReturn(true);
        given(memberRepository.existsByEmail(email + "fake")).willReturn(false);

        //when
        boolean isDuplicateEmailTrue = memberService.isDuplicateEmail(email);
        boolean isDuplicateEmailFalse = memberService.isDuplicateEmail(email + "fake");

        //then
        assertThat(isDuplicateEmailTrue).isTrue();
        assertThat(isDuplicateEmailFalse).isFalse();
        verify(memberRepository).existsByEmail(email);
        verify(memberRepository).existsByEmail(email+"fake");
    }

    @Test
    @DisplayName("전화번호 중복 테스트")
    void isDuplicatePhone() {
        // given
        given(memberRepository.existsByPhone(phone)).willReturn(true);
        given(memberRepository.existsByPhone(phone + "3")).willReturn(false);

        // when
        boolean isDuplicatePhoneTrue = memberService.isDuplicatePhone(phone);
        boolean isDuplicatePhoneFalse = memberService.isDuplicatePhone(phone + "3");

        //then
        assertThat(isDuplicatePhoneTrue).isTrue();
        assertThat(isDuplicatePhoneFalse).isFalse();
        verify(memberRepository).existsByPhone(phone);
        verify(memberRepository).existsByPhone(phone+"3");
    }

    @Test
    @DisplayName("회원 id로 존재여부 확인 테스트")
    void existsMemberById() {
        // given
        given(memberRepository.existsById(memberId)).willReturn(true);
        given(memberRepository.existsById(memberId + 1L)).willReturn(false);

        // when
        boolean existsMemberByIdTrue = memberService.existsMemberById(memberId);
        boolean existsMemberByIdFalse = memberService.existsMemberById(memberId + 1L);

        //then
        assertThat(existsMemberByIdTrue).isTrue();
        assertThat(existsMemberByIdFalse).isFalse();
        verify(memberRepository).existsById(memberId);
        verify(memberRepository).existsById(memberId+1L);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signup() {
        // given
        Member userMember = new Member(email, password, name, phone, birth, memberId);

        given(passwordEncoder.encode(password)).willReturn("encryptedPwd");
        given(memberRepository.save(Mockito.any(Member.class))).willReturn(userMember);
        given(adminMemberRepository.save(Mockito.any(AdminMember.class))).willReturn(new AdminMember());

        // when
        SignupDto dto = new SignupDto(email, password, name, phone, birth, role);
        Long memberId = memberService.signup(dto);

        SignupDto adminDto = new SignupDto(email, password, name, phone, birth, "admin");
        Long adminMemberId = memberService.signup(adminDto);

        // then
        assertThat(memberId).isEqualTo(userMember.getId());

        verify(memberRepository, times(2)).save(any(Member.class)); // 2번 호출
        verify(adminMemberRepository).save(any(AdminMember.class));
    }

    @Test
    @DisplayName("로그인 테스트")
    void login() {
        // given
        Member member = new Member(email, password, name, phone, birth, memberId);
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
        given(adminMemberRepository.existsByMember(member)).willReturn(false);
        given(passwordEncoder.matches(password, member.getPassword())).willReturn(true);
        given(tokenProvider.create(member)).willReturn(token);

        // when
        LoginDto login = memberService.login(email, password);

        // then
        assertThat(login.getToken()).isEqualTo(token);
        assertThat(login.getRole()).isEqualTo(role);

        verify(memberRepository).findByEmail(email);
        verify(adminMemberRepository).existsByMember(member);
        verify(passwordEncoder).matches(password, member.getPassword());
        verify(tokenProvider).create(member);
    }

    @Test
    @DisplayName("이메일 찾기 테스트")
    void findEmail() {
        // given
        Member member = new Member(email, password, name, phone, birth, memberId);
        given(memberRepository.findByNameAndPhone(name, phone)).willReturn(Optional.of(member));

        // when
        String findEmail = memberService.findEmail(name, phone);

        // then
        assertThat(findEmail).isEqualTo(email);

        verify(memberRepository).findByNameAndPhone(name, phone);
    }

    @Test
    @DisplayName("비밀번호 찾기 테스트")
    void findPassword() {
        // given
        Member member = new Member(email, password, name, phone, birth, memberId);
        given(memberRepository.findByEmailAndNameAndPhone(email, name, phone))
                .willReturn(Optional.of(member));
        given(passwordEncoder.encode(any(String.class)))
                .willReturn("tempPassword");

        //영문(소,대문자) + 숫자 + 특수문자 + 공백문자 허용X + (영문, 숫자, 특수문자 최소 1글자 이상)
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$)";

        // when
        String findPassword = memberService.findPassword(email, name, phone);


        // then
        assertThat(findPassword).hasSize(10)
                .containsPattern(Pattern.compile(pattern));

        verify(memberRepository).findByEmailAndNameAndPhone(email, name, phone);
        verify(passwordEncoder).encode(any(String.class));
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void changePassword() {
        // given
        Member member = new Member(email, password, name, phone, birth, memberId);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(passwordEncoder.matches("newPassword", member.getPassword())).willReturn(false);
        given(passwordEncoder.encode("newPassword")).willReturn("encoderPassword");

        // when
        memberService.changePassword(memberId, "newPassword");

        assertThat(member.getPassword()).isEqualTo("encoderPassword");

        verify(memberRepository).findById(memberId);
        verify(passwordEncoder).matches("newPassword", password);
        verify(passwordEncoder).encode("newPassword");
    }

    @Test
    @DisplayName("회원 id로 admin id 찾기")
    void findAdminByMemberId() {
        // given
        Member member = new Member(email, password, name, phone, birth, memberId);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(adminMemberRepository.findByMember(member))
                .willReturn(Optional.of(new AdminMember(member)));

        // when
        AdminMember admin = memberService.findAdminByMemberId(memberId);

        // then
        assertThat(admin.getMember())
                .usingRecursiveComparison() // 모든 필드값 비교
                .isEqualTo(member);

        verify(memberRepository).findById(memberId);
        verify(adminMemberRepository).findByMember(member);
    }

    @Test
    @DisplayName("포인트 찾기 테스트")
    void findPointByMemberId() {
        // given
        Member member = new Member(email, password, name, phone, birth, memberId);
        member.changePoint(500);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when
        Integer findPoint = memberService.findPointByMemberId(memberId);

        // then
        assertThat(findPoint).isEqualTo(500);

        verify(memberRepository).findById(memberId);
    }

    @Test
    @DisplayName("회원 정보 조회 테스트")
    void findInfoByUserId() {
        // given
        Member member = new Member(email, password, name, phone, birth, memberId);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when
        FindMemberInfoByUserIdDto result = memberService.findInfoByUserId(memberId);

        // then
        assertThat(result)
                .usingRecursiveComparison() // 모든 필드값 비교
                .ignoringFields("role")
                .isEqualTo(member);

        verify(memberRepository).findById(memberId);
    }
}