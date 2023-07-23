package konkuk.shop.domain.member.application;

import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.member.dto.LoginDto;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.exception.NotMatchPasswordException;
import konkuk.shop.domain.member.exception.UserNotFoundException;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.global.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberLoginServiceTest {

    private final String name = "testMember";
    private final String email = "asdf@asdf.com";
    private final String password = "asdfasdf@1";
    private final String phone = "01012345678";
    private final String birth = "20000327";
    private final String userRole = "user";
    private final String adminRole = "admin";
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
    MemberLoginService memberLoginService;

    @Test
    @DisplayName("로그인 테스트 - 일반 유저")
    void loginUser() {
        // given
        Member member = new Member(email, password, name, phone, birth);
        ReflectionTestUtils.setField(member, "id", memberId);
        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(member));
        given(adminMemberRepository.existsByMember(member))
                .willReturn(false);
        given(passwordEncoder.matches(password, member.getPassword()))
                .willReturn(true);
        given(tokenProvider.create(member))
                .willReturn(token);

        // when
        LoginDto.Response login = memberLoginService.login(email, password);

        // then
        assertThat(login.getToken()).isEqualTo(token);
        assertThat(login.getRole()).isEqualTo(userRole);

        verify(memberRepository).findByEmail(email);
        verify(adminMemberRepository).existsByMember(member);
        verify(passwordEncoder).matches(password, member.getPassword());
        verify(tokenProvider).create(member);
    }

    @Test
    @DisplayName("로그인 테스트 - 관리자")
    void loginAdmin() {
        // given
        Member member = new Member(email, password, name, phone, birth);
        ReflectionTestUtils.setField(member, "id", memberId);
        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(member));
        given(adminMemberRepository.existsByMember(member))
                .willReturn(true);
        given(passwordEncoder.matches(password, member.getPassword()))
                .willReturn(true);
        given(tokenProvider.create(member))
                .willReturn(token);

        // when
        LoginDto.Response login = memberLoginService.login(email, password);

        // then
        assertThat(login.getToken()).isEqualTo(token);
        assertThat(login.getRole()).isEqualTo(adminRole);
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void loginFailByNotFoundEmail() {
        // given
        given(memberRepository.findByEmail(email))
                .willReturn(Optional.empty());

        // expected
        assertThrows(UserNotFoundException.class,
                () -> memberLoginService.login(email, password));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void loginFailByNotEqualPassword() {
        // given
        Member member = new Member(email, password, name, phone, birth);
        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(member));
        given(passwordEncoder.matches(password, member.getPassword()))
                .willReturn(false);

        // expected
        assertThrows(NotMatchPasswordException.class,
                () -> memberLoginService.login(email, password));
    }
}