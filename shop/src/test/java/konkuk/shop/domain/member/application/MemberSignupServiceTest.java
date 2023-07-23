package konkuk.shop.domain.member.application;

import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.member.dto.SignupDto;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.entity.MemberRole;
import konkuk.shop.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberSignupServiceTest {
    private final String name = "testMember";
    private final String email = "asdf@asdf.com";
    private final String password = "asdfasdf@1";
    private final String phone = "01012345678";
    private final String birth = "20000327";
    private final String role = "user";
    private final Long memberId = 3L;
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AdminMemberRepository adminMemberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberSignupService memberSignupService;

    @Test
    @DisplayName("회원가입 테스트")
    void signup() {
        // given
        given(passwordEncoder.encode(password))
                .willReturn("encryptedPwd");

        Member member = new Member(email, password, name, phone, birth);
        member.setMemberRole(MemberRole.BRONZE);
        ReflectionTestUtils.setField(member, "id", memberId);
        given(memberRepository.save(any(Member.class)))
                .willReturn(member);

        // when
        SignupDto.Request dto = new SignupDto.Request(email, password, name, phone, birth, role);
        Long memberId = memberSignupService.signup(dto);

        // then
        assertThat(memberId).isEqualTo(member.getId());
    }
}