package konkuk.shop.domain.member.application;

import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.member.dto.LoginDto;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.exception.NotMatchPasswordException;
import konkuk.shop.domain.member.exception.UserNotFoundException;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberLoginService {
    private final MemberRepository memberRepository;
    private final AdminMemberRepository adminMemberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public LoginDto.Response login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        validPassword(password, member);

        String token = tokenProvider.create(member);
        String role = getRole(member);
        return new LoginDto.Response(token, role);
    }

    private String getRole(Member member) {
        if (adminMemberRepository.existsByMember(member)) {
            return "admin";
        }
        return "user";
    }

    private void validPassword(String password, Member findMember) {
        boolean isPasswordMatch = passwordEncoder.matches(password, findMember.getPassword());
        if (!isPasswordMatch) {
            throw new NotMatchPasswordException();
        }
    }


}
