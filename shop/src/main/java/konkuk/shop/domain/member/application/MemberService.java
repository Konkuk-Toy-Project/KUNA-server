package konkuk.shop.domain.member.application;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.member.dto.LoginDto;
import konkuk.shop.domain.member.dto.SignupDto;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.entity.MemberRole;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.dto.FindMemberInfoByUserIdDto;
import konkuk.shop.global.error.ApiException;
import konkuk.shop.global.error.ExceptionEnum;
import konkuk.shop.global.security.TokenProvider;
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

    public boolean existsMemberById(Long id) {
        return memberRepository.existsById(id);
    }

    @Transactional
    public Long signup(SignupDto.Request dto) {
        signUpValidation(dto);

        log.info("회원가입 요청. email={}, password={}", dto.getEmail(), dto.getPassword());
        String encryptedPwd = passwordEncoder.encode(dto.getPassword());
        Member saveMember = memberRepository.save
                (new Member(dto.getEmail(), encryptedPwd, dto.getName(), dto.getPhone(), dto.getBirth()));

        if (dto.getRole().equals("user")) saveMember.setMemberRole(MemberRole.BRONZE);
        else if (dto.getRole().equals("admin")) {
            saveMember.setMemberRole(MemberRole.ADMIN);
            AdminMember adminMember = new AdminMember(saveMember);
            adminMemberRepository.save(adminMember);
        }
        return saveMember.getId();
    }

    public LoginDto.Response login(String email, String password) {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER_EMAIL));
        boolean passwordMatch = passwordEncoder.matches(password, findMember.getPassword());
        if (!passwordMatch) throw new ApiException(ExceptionEnum.NO_MATCH_MEMBER_PASSWORD);

        String token = tokenProvider.create(findMember);
        String role = "user";
        if (adminMemberRepository.existsByMember(findMember)) role = "admin";
        log.info("로그인 요청. memberId={}, role={}", findMember.getId(), role);
        return new LoginDto.Response(token, role);
    }

    public String findEmail(String name, String phone) {
        log.info("이메일 찾기 기능. name={}, phone={}", name, phone);
        return memberRepository.findByNameAndPhone(name, phone)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER))
                .getEmail();
    }

    @Transactional
    public String findPassword(String email, String name, String phone) {
        Member member = memberRepository.findByEmailAndNameAndPhone(email, name, phone)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        String tempPassword = randomPw();
        member.setPassword(passwordEncoder.encode(tempPassword));
        log.info("임시 비밀번호 발급. newPassword={}", tempPassword);

        return tempPassword;
    }

    @Transactional
    public void changePassword(Long userId, String newPassword) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        boolean match = passwordEncoder.matches(newPassword, member.getPassword());
        if (match) throw new ApiException(ExceptionEnum.NOTHING_CHANGE_PASSWORD);

        log.info("비밀번호 변경. newPassword={}", newPassword);
        member.setPassword(passwordEncoder.encode(newPassword));
    }

    private String randomPw() {
        char[] pwCollectionSpCha = new char[]{'!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};
        char[] pwCollectionNum = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',};
        char[] pwCollectionAll = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};
        return getRandPw(1, pwCollectionSpCha) + getRandPw(8, pwCollectionAll) + getRandPw(1, pwCollectionNum);
    }

    private String getRandPw(int size, char[] pwCollection) {
        StringBuilder ranPw = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int selectRandomPw = (int) (Math.random() * (pwCollection.length));
            ranPw.append(pwCollection[selectRandomPw]);
        }
        return ranPw.toString();
    }

    public AdminMember findAdminByMemberId(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        return adminMemberRepository.findByMember(member)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NOT_ADMIN_MEMBER));
    }

    private void signUpValidation(SignupDto.Request dto) {
        if (!dto.getRole().equals("user") && !dto.getRole().equals("admin"))
            throw new ApiException(ExceptionEnum.NOT_FIND_ROLE);
        if (isDuplicateEmail(dto.getEmail())) throw new ApiException(ExceptionEnum.DUPLICATION_MEMBER_EMAIL);
        if (isDuplicatePhone(dto.getPhone())) throw new ApiException(ExceptionEnum.DUPLICATION_MEMBER_PHONE);

        if (!dto.getEmail().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"))
            throw new ApiException(ExceptionEnum.NOT_EMAIL_FORM);

        if (!dto.getPassword().matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$"))
            throw new ApiException(ExceptionEnum.NOT_PASSWORD_FORM);

        if (dto.getName().contains(" ")) throw new ApiException(ExceptionEnum.NOT_NAME_FORM);

        if (!dto.getPhone().matches("^[0-9]*$") || dto.getPhone().length() < 9)
            throw new ApiException(ExceptionEnum.NOT_PHONE_FORM);

        if (!dto.getBirth().matches("^[0-9]*$") || dto.getBirth().length() != 8)
            throw new ApiException(ExceptionEnum.NOT_BIRTH_FORM);
    }

    public Integer findPointByMemberId(Long userId) {
        log.info("포인트 조회. memberId={}", userId);
        return memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER))
                .getPoint();
    }

    public FindMemberInfoByUserIdDto findInfoByUserId(Long userId) {
        log.info("사용자 정보 조회. memberId={}", userId);
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));

        return FindMemberInfoByUserIdDto.builder()
                .birth(member.getBirth())
                .name(member.getName())
                .phone(member.getPhone())
                .role(member.getMemberRole())
                .email(member.getEmail())
                .build();
    }
}
