package konkuk.shop;

import konkuk.shop.dto.SignupDto;
import konkuk.shop.service.ItemService;
import konkuk.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitDB {
    private final MemberService memberService;
    private final ItemService itemService;



    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        log.info("initialize database");
        initMember();
    }

    public void initMember() {
        SignupDto dto1=new SignupDto("asdf@asdf.com", "asdfasdf@", "testMember1", "01012345678", "20000327");
        SignupDto dto2=new SignupDto("asdf2@asdf.com", "asdfasdf@", "testMember2", "01087654321", "19991003");
        memberService.signup(dto1);
        memberService.signup(dto2);
    }

    public void initItem() {

    }

}