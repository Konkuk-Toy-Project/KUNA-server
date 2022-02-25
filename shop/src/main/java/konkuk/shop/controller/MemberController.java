package konkuk.shop.controller;

import konkuk.shop.dto.FindMemberInfoByUserIdDto;
import konkuk.shop.dto.LoginDto;
import konkuk.shop.dto.SignupDto;
import konkuk.shop.form.requestForm.member.*;
import konkuk.shop.form.responseForm.member.ResponseSignupForm;
import konkuk.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseSignupForm> memberSignup(@RequestBody RequestSignupForm form) {
        ModelMapper mapper = new ModelMapper();
        Long saveMemberId = memberService.signup(mapper.map(form, SignupDto.class));
        ResponseSignupForm result = new ResponseSignupForm(form.getRole(), saveMemberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/duplication/email")
    public HashMap<String, Object> isDuplicationEmail(@RequestBody RequestIsDuplicationEmail form) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("isDuplication", memberService.isDuplicateEmail(form.getEmail()));

        return result;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@RequestBody RequestLoginForm form) {
        LoginDto result = memberService.login(form.getEmail(), form.getPassword());

        return ResponseEntity.status(HttpStatus.OK).body(result);
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

    @PostMapping("/change/password")
    public void changePassword(@AuthenticationPrincipal Long userId,
                               @RequestBody RequestChangePasswordForm form) {
        memberService.changePassword(userId, form.getNewPassword());
    }

    @GetMapping("/point")
    public HashMap<String, Object> findPoint(@AuthenticationPrincipal Long userId) {
        Integer point = memberService.findPointByMemberId(userId);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("point", point);
        return result;
    }

    @GetMapping("/info")
    public ResponseEntity<FindMemberInfoByUserIdDto> findLoginMemberInfo(@AuthenticationPrincipal Long userId) {
        FindMemberInfoByUserIdDto result = memberService.findInfoByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/isLogin")
    public HashMap<String, Object> isLogin(@AuthenticationPrincipal Long userId) {
        log.info("로그인 여부 요청.");
        HashMap<String, Object> result = new HashMap<String, Object>();

        if(userId==null) result.put("isLogin", false);
        else{
            boolean isLogin = memberService.existsMemberById(userId);
            result.put("isLogin", isLogin);
        }
        return result;
    }
}
