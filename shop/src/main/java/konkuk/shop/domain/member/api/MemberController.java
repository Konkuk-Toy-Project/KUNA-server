package konkuk.shop.domain.member.api;

import konkuk.shop.domain.member.application.MemberFindInfoService;
import konkuk.shop.domain.member.application.MemberLoginService;
import konkuk.shop.domain.member.application.MemberSignupService;
import konkuk.shop.domain.member.application.MemberUpdateAccountService;
import konkuk.shop.domain.member.dto.*;
import konkuk.shop.dto.FindMemberInfoByUserIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberUpdateAccountService memberUpdateAccountService;
    private final MemberSignupService memberSignupService;
    private final MemberLoginService memberLoginService;
    private final MemberFindInfoService memberFindInfoService;

    @PostMapping("/signup")
    public ResponseEntity<SignupDto.Response> memberSignup(@Valid @RequestBody SignupDto.Request request) {
        Long saveMemberId = memberSignupService.signup(request);
        SignupDto.Response response = new SignupDto.Response(request.getRole(), saveMemberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/duplication/email")
    public ResponseEntity<DuplicationEmailDto.Response> checkDuplicationEmail(@RequestBody DuplicationEmailDto.Request request) {
        boolean isDuplicationEmail = memberFindInfoService.isDuplicateEmail(request.getEmail());
        DuplicationEmailDto.Response response = new DuplicationEmailDto.Response(isDuplicationEmail);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto.Response> login(@RequestBody LoginDto.Request form) {
        LoginDto.Response response = memberLoginService.login(form.getEmail(), form.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/find/email")
    public ResponseEntity<FindEmailDto.Response> findEmail(@RequestBody FindEmailDto.Request form) {
        String email = memberFindInfoService.findEmail(form.getName(), form.getPhone());
        FindEmailDto.Response response = new FindEmailDto.Response(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/find/password")
    public ResponseEntity<FindPasswordDto.Response> findPassword(@RequestBody FindPasswordDto.Request form) {
        String tempPassword = memberUpdateAccountService.updateTemporaryPassword(form.getEmail(), form.getName(), form.getPhone());
        FindPasswordDto.Response response = new FindPasswordDto.Response(tempPassword);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change/password")
    public void changePassword(@AuthenticationPrincipal Long userId,
                               @Valid @RequestBody ChangePasswordDto form) {
        memberUpdateAccountService.changePassword(userId, form.getNewPassword());
    }

    @GetMapping("/point")
    public ResponseEntity<FindPointDto> findPoint(@AuthenticationPrincipal Long userId) {
        Integer point = memberFindInfoService.findPointByMemberId(userId);
        FindPointDto response = new FindPointDto(point);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<FindMemberInfoByUserIdDto> findLoginMemberInfo(@AuthenticationPrincipal Long userId) {
        FindMemberInfoByUserIdDto response = memberFindInfoService.findInfoByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/isLogin")
    public ResponseEntity<LoginCheckDto> isLogin(@AuthenticationPrincipal Long userId) {
        boolean isLogin = false;
        if (userId != null) {
            isLogin = memberFindInfoService.existsMemberById(userId);
        }
        LoginCheckDto response = new LoginCheckDto(isLogin);
        return ResponseEntity.ok(response);
    }
}
