package konkuk.shop.controller;

import konkuk.shop.dto.SignupDto;
import konkuk.shop.entity.Member;
import konkuk.shop.form.requestForm.*;
import konkuk.shop.form.responseForm.ResponseSignupForm;
import konkuk.shop.security.TokenProvider;
import konkuk.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final Environment env;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working Api Server"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", with token secret=" + env.getProperty("token.secret")
                + ", with token time=" + env.getProperty("token.expiration_time"));
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseSignupForm> memberSignup(@RequestBody RequestSignupForm form) {
        ModelMapper mapper = new ModelMapper();
        Long saveMemberId = memberService.signup(mapper.map(form, SignupDto.class));
        ResponseSignupForm result = new ResponseSignupForm(true, saveMemberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/duplication/email")
    public HashMap<String, Object> isDuplicationEmail(@RequestBody RequestIsDuplicationEmail form) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("isDuplication", memberService.isDuplicateEmail(form.getEmail()));

        return result;
    }

    @PostMapping("/login")
    public HashMap<String, Object> isDuplicationEmail(@RequestBody RequestLoginForm form) {
        Member loginMember = memberService.login(form.getEmail(), form.getPassword());
        String token = tokenProvider.create(loginMember);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("token", token);
        return result;
    }

    @PostMapping("/find/email")
    public HashMap<String, Object> findEmail(@RequestBody RequestFindEmailForm form) {
        String email = memberService.findEmail(form.getName(), form.getPhone());

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("email", email);
        return result;
    }

    @PostMapping("/find/password")
    public HashMap<String, Object> findPassword(@RequestBody RequestFindPasswordForm form) {
        String tempPassword = memberService.findPassword(form.getEmail(), form.getName(), form.getPhone());

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("tempPassword", tempPassword);
        return result;
    }


}
