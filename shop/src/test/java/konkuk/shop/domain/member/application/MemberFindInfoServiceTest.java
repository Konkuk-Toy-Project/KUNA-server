package konkuk.shop.domain.member.application;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.dto.FindMemberInfoByUserIdDto;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberFindInfoServiceTest {
    private final String name = "testMember";
    private final String email = "asdf@asdf.com";
    private final String password = "asdfasdf@1";
    private final String phone = "01012345678";
    private final String birth = "20000327";
    private final Long memberId = 3L;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AdminMemberRepository adminMemberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private MemberFindInfoService memberFindInfoService;

    @Test
    @DisplayName("이메일 중복 테스트 - 중복")
    void duplicateEmail() {
        //given
        given(memberRepository.existsByEmail(email))
                .willReturn(true);

        //when
        boolean isDuplicateEmailTrue = memberFindInfoService.isDuplicateEmail(email);

        //then
        assertThat(isDuplicateEmailTrue).isTrue();
        verify(memberRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("이메일 중복 테스트 - 중복 아님")
    void notDuplicateEmail() {
        //given
        given(memberRepository.existsByEmail(email + "fake"))
                .willReturn(false);

        //when
        boolean isDuplicateEmailFalse = memberFindInfoService.isDuplicateEmail(email + "fake");

        //then
        assertThat(isDuplicateEmailFalse).isFalse();
        verify(memberRepository).existsByEmail(email + "fake");
    }

    @Test
    @DisplayName("회원 id로 존재여부 확인 테스트 - 존재")
    void existsMemberById() {
        // given
        given(memberRepository.existsById(memberId))
                .willReturn(true);

        // when
        boolean existsMemberByIdTrue = memberFindInfoService.existsMemberById(memberId);

        //then
        assertThat(existsMemberByIdTrue).isTrue();
        verify(memberRepository).existsById(memberId);
    }

    @Test
    @DisplayName("회원 id로 존재여부 확인 테스트 - 존재 안함")
    void notExistsMemberById() {
        // given
        given(memberRepository.existsById(memberId + 1L))
                .willReturn(false);

        // when
        boolean existsMemberByIdFalse = memberFindInfoService.existsMemberById(memberId + 1L);

        //then
        assertThat(existsMemberByIdFalse).isFalse();
        verify(memberRepository).existsById(memberId + 1L);
    }

    @Test
    @DisplayName("이메일 찾기 테스트")
    void findEmail() {
        // given
        Member member = new Member(email, password, name, phone, birth);
        ReflectionTestUtils.setField(member, "id", memberId);

        given(memberRepository.findByNameAndPhone(name, phone))
                .willReturn(Optional.of(member));

        // when
        String findEmail = memberFindInfoService.findEmail(name, phone);

        // then
        assertThat(findEmail).isEqualTo(email);
        verify(memberRepository).findByNameAndPhone(name, phone);
    }

    @Test
    @DisplayName("회원 id로 admin id 찾기")
    void findAdminByMemberId() {
        // given
        Member member = new Member(email, password, name, phone, birth);
        ReflectionTestUtils.setField(member, "id", memberId);

        given(adminMemberRepository.findByMemberId(memberId))
                .willReturn(Optional.of(new AdminMember(member)));

        // when
        AdminMember admin = memberFindInfoService.findAdminByMemberId(memberId);

        // then
        assertThat(admin.getMember())
                .usingRecursiveComparison() // 모든 필드값 비교
                .isEqualTo(member);
    }

    @Test
    @DisplayName("포인트 찾기 테스트")
    void findPointByMemberId() {
        // given
        Member member = new Member(email, password, name, phone, birth);
        member.changePoint(500);
        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

        // when
        Integer findPoint = memberFindInfoService.findPointByMemberId(memberId);

        // then
        assertThat(findPoint).isEqualTo(500);
        verify(memberRepository).findById(memberId);
    }

    @Test
    @DisplayName("회원 정보 조회 테스트")
    void findInfoByUserId() {
        // given
        Member member = new Member(email, password, name, phone, birth);
        ReflectionTestUtils.setField(member, "id", memberId);
        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

        // when
        FindMemberInfoByUserIdDto result = memberFindInfoService.findInfoByUserId(memberId);

        // then
        assertThat(result)
                .usingRecursiveComparison() // 모든 필드값 비교
                .ignoringFields("role")
                .isEqualTo(member);
        verify(memberRepository).findById(memberId);
    }
}