package konkuk.shop;

import konkuk.shop.entity.Member;
import konkuk.shop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class test {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void memberIdIsUUID() {
        //given
        Member member = new Member();

        //when
        Member saveMember = memberRepository.save(member);

        //then
        //assertThat(average).isEqualTo(6);
        System.out.println("saveMemberId = "+saveMember.getId());
    }
}