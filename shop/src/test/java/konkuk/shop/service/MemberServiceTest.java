package konkuk.shop.service;

import konkuk.shop.repository.AdminMemberRepository;
import konkuk.shop.repository.MemberRepository;
import konkuk.shop.security.TokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {MemberService.class}) //매개변수로 클래스를 안주면 스프링부트 전체가 뜬다.
//@ExtendWith(SpringExtension.class)
//@Import({MemberService.class})
class MemberServiceTest {
    @MockBean
    MemberRepository memberRepository;

    @MockBean
    BCryptPasswordEncoder passwordEncoder;

    @MockBean
    TokenProvider tokenProvider;

    @MockBean
    AdminMemberRepository adminMemberRepository;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("중복 이메일 확인 테스트")
    void isDuplicateEmail() {
        //given
        Mockito.when(memberRepository.existsByEmail("asdf@asdf.com"))
                .thenReturn(true);
        //when
        boolean isDuplicateEmail = memberService.isDuplicateEmail("asdf@asdf.com");

        //then
        assertTrue(isDuplicateEmail);

        verify(memberRepository).existsByEmail("asdf@asdf.com");
    }
}