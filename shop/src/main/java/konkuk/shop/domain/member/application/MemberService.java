package konkuk.shop.domain.member.application;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.dto.FindMemberInfoByUserIdDto;
import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;
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

    public boolean isDuplicateEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean isDuplicatePhone(String phone) {
        return memberRepository.existsByPhone(phone);
    }

    public boolean existsMemberById(Long id) {
        return memberRepository.existsById(id);
    }

    public String findEmail(String name, String phone) {
        log.info("이메일 찾기 기능. name={}, phone={}", name, phone);
        return memberRepository.findByNameAndPhone(name, phone)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_MEMBER))
                .getEmail();
    }

    @Transactional
    public String findPassword(String email, String name, String phone) {
        Member member = memberRepository.findByEmailAndNameAndPhone(email, name, phone)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_MEMBER));
        String tempPassword = randomPw();
        member.setPassword(passwordEncoder.encode(tempPassword));
        log.info("임시 비밀번호 발급. newPassword={}", tempPassword);

        return tempPassword;
    }

    @Transactional
    public void changePassword(Long userId, String newPassword) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_MEMBER));
        boolean match = passwordEncoder.matches(newPassword, member.getPassword());
        if (match) throw new ApplicationException(ErrorCode.NOTHING_CHANGE_PASSWORD);

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
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_MEMBER));
        return adminMemberRepository.findByMember(member)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_ADMIN_MEMBER));
    }

    public Integer findPointByMemberId(Long userId) {
        log.info("포인트 조회. memberId={}", userId);
        return memberRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_MEMBER))
                .getPoint();
    }

    public FindMemberInfoByUserIdDto findInfoByUserId(Long userId) {
        log.info("사용자 정보 조회. memberId={}", userId);
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_MEMBER));

        return FindMemberInfoByUserIdDto.builder()
                .birth(member.getBirth())
                .name(member.getName())
                .phone(member.getPhone())
                .role(member.getMemberRole())
                .email(member.getEmail())
                .build();
    }
}