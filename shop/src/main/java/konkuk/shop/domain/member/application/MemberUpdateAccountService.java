package konkuk.shop.domain.member.application;

import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.exception.NothingChangePasswordException;
import konkuk.shop.domain.member.exception.UserNotFoundException;
import konkuk.shop.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberUpdateAccountService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public String updateTemporaryPassword(String email, String name, String phone) {
        Member member = memberRepository.findByEmailAndNameAndPhone(email, name, phone)
                .orElseThrow(UserNotFoundException::new);
        String tempPassword = randomPw();
        member.setPassword(passwordEncoder.encode(tempPassword));
        return tempPassword;
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

    @Transactional
    public void changePassword(Long userId, String newPassword) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        validCompareNewWithCurrentPassword(newPassword, member);

        String encryptedPwd = passwordEncoder.encode(newPassword);
        member.setPassword(encryptedPwd);
    }

    private void validCompareNewWithCurrentPassword(String newPassword, Member member) {
        boolean match = passwordEncoder.matches(newPassword, member.getPassword());
        if (match) {
            throw new NothingChangePasswordException();
        }
    }

}
