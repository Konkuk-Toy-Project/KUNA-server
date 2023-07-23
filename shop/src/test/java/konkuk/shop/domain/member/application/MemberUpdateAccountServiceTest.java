package konkuk.shop.domain.member.application;

import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberUpdateAccountServiceTest {
    private final String name = "testMember";
    private final String email = "asdf@asdf.com";
    private final String password = "asdfasdf@1";
    private final String phone = "01012345678";
    private final String birth = "20000327";
    private final Long memberId = 3L;

    @Mock
    MemberRepository memberRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    MemberUpdateAccountService memberUpdateAccountService;

    @Test
    @DisplayName("임시 비밀번호 발급 테스트")
    void findPassword() {
        // given
        Member member = new Member(email, password, name, phone, birth);
        ReflectionTestUtils.setField(member, "id", memberId);

        given(memberRepository.findByEmailAndNameAndPhone(email, name, phone))
                .willReturn(Optional.of(member));
        given(passwordEncoder.encode(any(String.class)))
                .willReturn("tempPassword");

        // when
        String findPassword = memberUpdateAccountService.updateTemporaryPassword(email, name, phone);

        // then
        //영문(소,대문자) + 숫자 + 특수문자 + 공백문자 허용X + (영문, 숫자, 특수문자 최소 1글자 이상)
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$)";
        assertThat(findPassword).hasSize(10)
                .containsPattern(Pattern.compile(pattern));

        verify(memberRepository).findByEmailAndNameAndPhone(email, name, phone);
        verify(passwordEncoder).encode(any(String.class));
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void changePassword() {
        // given
        Member member = new Member(email, password, name, phone, birth);
        ReflectionTestUtils.setField(member, "id", memberId);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(passwordEncoder.matches("newPassword", member.getPassword())).willReturn(false);
        given(passwordEncoder.encode("newPassword")).willReturn("encoderPassword");

        // when
        memberUpdateAccountService.changePassword(memberId, "newPassword");

        assertThat(member.getPassword()).isEqualTo("encoderPassword");

        verify(memberRepository).findById(memberId);
        verify(passwordEncoder).matches("newPassword", password);
        verify(passwordEncoder).encode("newPassword");
    }

}