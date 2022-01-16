package konkuk.shop.service;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import konkuk.shop.dto.SignupDto;
import konkuk.shop.entity.Member;
import konkuk.shop.entity.MemberRole;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment env;


    public boolean isDuplicateEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    public Long signup(SignupDto dto) {
        if(isDuplicateEmail(dto.getEmail())) throw new ApiException(ExceptionEnum.DUPLICATION_MEMBER_EMAIL);

        String encryptedPwd = passwordEncoder.encode(dto.getPassword());
        Member member = new Member(dto.getEmail(), encryptedPwd, dto.getName(), dto.getPhone(), dto.getBirth());
        return memberRepository.save(member).getId();
    }

    public String login(String email, String password) {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER_EMAIL));
        boolean match = passwordEncoder.matches(password, findMember.getPassword());
        if(!match) throw new ApiException(ExceptionEnum.NO_MATCH_MEMBER_PASSWORD);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(new Date())
                .claim("email", email)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();
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

    public static String randomPw(){
        char pwCollectionSpCha[]  = new char[] {'!','@','#','$','%','^','&','*','(',')'};
        char pwCollectionNum[]   = new char[] {'1','2','3','4','5','6','7','8','9','0',};
        char pwCollectionAll[]  = new char[] {'1','2','3','4','5','6','7','8','9','0',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                '!','@','#','$','%','^','&','*','(',')'};
        return getRandPw(1, pwCollectionSpCha) + getRandPw(8, pwCollectionAll) + getRandPw(1, pwCollectionNum);
    }

    public static String getRandPw(int size, char[] pwCollection){
        String ranPw = "";
        for (int i = 0; i < size; i++) {
            int selectRandomPw = (int) (Math.random() * (pwCollection.length));
            ranPw += pwCollection[selectRandomPw];
        }
        return ranPw;
    }
}
