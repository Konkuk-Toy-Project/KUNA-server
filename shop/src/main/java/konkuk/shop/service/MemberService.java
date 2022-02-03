package konkuk.shop.service;

import konkuk.shop.dto.LoginDto;
import konkuk.shop.dto.SignupDto;
import konkuk.shop.entity.AdminMember;
import konkuk.shop.entity.Member;
import konkuk.shop.entity.MemberRole;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.repository.AdminMemberRepository;
import konkuk.shop.repository.MemberRepository;
import konkuk.shop.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AdminMemberRepository adminMemberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public boolean isDuplicateEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean isDuplicatePhone(String phone) {
        return memberRepository.existsByPhone(phone);
    }

    @Transactional
    public Long signup(SignupDto dto) {
        signUpValidation(dto);

        String encryptedPwd = passwordEncoder.encode(dto.getPassword());
        Member member = new Member(dto.getEmail(), encryptedPwd, dto.getName(), dto.getPhone(), dto.getBirth());
        Member saveMember = memberRepository.save(member);

        if (dto.getRole().equals("user")) saveMember.setMemberRole(MemberRole.BRONZE);
        else if (dto.getRole().equals("admin")) {
            saveMember.setMemberRole(MemberRole.ADMIN);
            AdminMember adminMember = new AdminMember(saveMember);
            adminMemberRepository.save(adminMember);
        }
        return saveMember.getId();
    }

    public LoginDto login(String email, String password) {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER_EMAIL));
        boolean match = passwordEncoder.matches(password, findMember.getPassword());
        if (!match) throw new ApiException(ExceptionEnum.NO_MATCH_MEMBER_PASSWORD);

        String token = tokenProvider.create(findMember);
        String role = "user";
        if (adminMemberRepository.existsByMember(findMember)) role = "admin";
        return new LoginDto(token, role);
    }

    public String findEmail(String name, String phone) {
        Member member = memberRepository.findByNameAndPhone(name, phone)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        return member.getEmail();
    }

    public String findPassword(String email, String name, String phone) {
        Member member = memberRepository.findByEmailAndNameAndPhone(email, name, phone)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        String tempPassword = randomPw();
        member.setPassword(passwordEncoder.encode(tempPassword));
        memberRepository.save(member);

        return tempPassword;
    }

    public void changePassword(Long userId, String newPassword) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        boolean match = passwordEncoder.matches(newPassword, member.getPassword());
        if (match) throw new ApiException(ExceptionEnum.NOTHING_CHANGE_PASSWORD);

        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }

//    public AdminMember addAdminMember(Long userId) {
//        Member member = memberRepository.findById(userId)
//                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
//        member.setMemberRole(MemberRole.ADMIN);
//
//        AdminMember adminMember = new AdminMember(member);
//        memberRepository.save(member);
//        return adminMemberRepository.save(adminMember);
//    }

    private String randomPw() {
        char pwCollectionSpCha[] = new char[]{'!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};
        char pwCollectionNum[] = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',};
        char pwCollectionAll[] = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};
        return getRandPw(1, pwCollectionSpCha) + getRandPw(8, pwCollectionAll) + getRandPw(1, pwCollectionNum);
    }

    private String getRandPw(int size, char[] pwCollection) {
        String ranPw = "";
        for (int i = 0; i < size; i++) {
            int selectRandomPw = (int) (Math.random() * (pwCollection.length));
            ranPw += pwCollection[selectRandomPw];
        }
        return ranPw;
    }

    public AdminMember findAdminByMemberId(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        return adminMemberRepository.findByMember(member)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NOT_ADMIN_MEMBER));
    }

    private void signUpValidation(SignupDto dto) {
        if (!dto.getRole().equals("user") && !dto.getRole().equals("admin"))
            throw new ApiException(ExceptionEnum.NOT_FIND_ROLE);
        if (isDuplicateEmail(dto.getEmail())) throw new ApiException(ExceptionEnum.DUPLICATION_MEMBER_EMAIL);
        if (isDuplicatePhone(dto.getPhone())) throw new ApiException(ExceptionEnum.DUPLICATION_MEMBER_PHONE);

        if (!dto.getEmail().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"))
            throw new ApiException(ExceptionEnum.NOT_EMAIL_FORM);

        if (!dto.getPassword().matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$"))
            throw new ApiException(ExceptionEnum.NOT_PASSWORD_FORM);

        if (dto.getName().contains(" ")) throw new ApiException(ExceptionEnum.NOT_NAME_FORM);

        if (!dto.getPhone().matches("^01(?:0|1|[6-9]) - (?:\\d{3}|\\d{4}) - \\d{4}$"))
            throw new ApiException(ExceptionEnum.NOT_PHONE_FORM);

        if (!dto.getBirth().matches("^[0-9]*$") || dto.getBirth().length() != 8)
            throw new ApiException(ExceptionEnum.NOT_BIRTH_FORM);
    }
}
