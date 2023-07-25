package konkuk.shop.domain.member.application;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.member.dto.SignupDto;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.entity.MemberRole;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberSignupService {
    private final MemberRepository memberRepository;
    private final AdminMemberRepository adminMemberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Long signup(SignupDto.Request dto) {
        signUpValidation(dto);

        String encryptedPwd = passwordEncoder.encode(dto.getPassword());
        Member member = new Member(dto.getEmail(), encryptedPwd, dto.getName(), dto.getPhone(), dto.getBirth());
        setMemberRole(dto.getRole(), member);
        member = memberRepository.save(member);
        saveAdminIfNecessary(member);
        return member.getId();
    }

    private void saveAdminIfNecessary(Member member) {
        if (member.getMemberRole().equals(MemberRole.ADMIN)) {
            AdminMember adminMember = new AdminMember(member);
            adminMemberRepository.save(adminMember);
        }
    }

    private void setMemberRole(String role, Member member) {
        MemberRole memberRole = MemberRole.convertToMemberRole(role);
        member.setMemberRole(memberRole);
    }

    private void signUpValidation(SignupDto.Request dto) {
        if (!dto.getRole().equals("user") && !dto.getRole().equals("admin")) {
            throw new ApplicationException(ErrorCode.NOT_FIND_ROLE);
        }
        if (isDuplicateEmail(dto.getEmail())) {
            throw new ApplicationException(ErrorCode.DUPLICATION_MEMBER_EMAIL);
        }
        if (isDuplicatePhone(dto.getPhone())) {
            throw new ApplicationException(ErrorCode.DUPLICATION_MEMBER_PHONE);
        }
    }

    private boolean isDuplicateEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    private boolean isDuplicatePhone(String phone) {
        return memberRepository.existsByPhone(phone);
    }
}
