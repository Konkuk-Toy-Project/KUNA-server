package konkuk.shop.domain.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.shop.WithAuthUser;
import konkuk.shop.domain.member.api.MemberController;
import konkuk.shop.domain.member.application.MemberFindInfoService;
import konkuk.shop.domain.member.application.MemberLoginService;
import konkuk.shop.domain.member.application.MemberUpdateAccountService;
import konkuk.shop.domain.member.application.MemberSignupService;
import konkuk.shop.domain.member.dto.*;
import konkuk.shop.domain.member.entity.MemberRole;
import konkuk.shop.dto.FindMemberInfoByUserIdDto;
import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;
import konkuk.shop.global.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 매개변수가 없으면 @Controller와 @RestController, @ControllerAdvice 등 Web과 관련된 설정된 클래스를 찾아 메모리에 생성한다.
// @WebMvcTest()
@WebMvcTest(MemberController.class)
class MemberControllerTest {
    private final String name = "testMember";
    private final String email = "asdf@asdf.com";
    private final String password = "asdfasdf@1";
    private final String phone = "01012345678";
    private final String birth = "20000327";
    private final String role = "user";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberUpdateAccountService memberUpdateAccountService;

    @MockBean
    private MemberSignupService memberSignupService;

    @MockBean
    private MemberLoginService memberLoginService;

    @MockBean
    private MemberFindInfoService memberFindInfoService;

    @MockBean
    private TokenProvider tokenProvider;


    @Test
    @DisplayName("이메일 중복 테스트 - 중복")
    void duplicationEmail() throws Exception {
        given(memberFindInfoService.isDuplicateEmail(email))
                .willReturn(true);

        DuplicationEmailDto.Request request = new DuplicationEmailDto.Request(email);
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/member/duplication/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDuplication").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 중복 테스트 - 중복 아님")
    void notDuplicationEmail() throws Exception {
        given(memberFindInfoService.isDuplicateEmail(email))
                .willReturn(false);

        DuplicationEmailDto.Request request = new DuplicationEmailDto.Request(email);
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/member/duplication/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDuplication").value(false))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 테스트")
    void memberSignup() throws Exception {
        final long memberId = 1234L;
        given(memberSignupService.signup(any(SignupDto.Request.class)))
                .willReturn(memberId);

        SignupDto.Request request = new SignupDto.Request(email, password, name, phone, birth, role);
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value(role))
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 테스트 - 로그인 성공")
    void loginSuccess() throws Exception {
        final String token = "jwtToken";
        given(memberLoginService.login(email, password))
                .willReturn(new LoginDto.Response(token, role));

        LoginDto.Request request = new LoginDto.Request(email, password);
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.role").value(role))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 테스트 - 로그인 실패")
    void loginFail() throws Exception {
        given(memberLoginService.login(email, password))
                .willThrow(new ApplicationException(ErrorCode.NO_MATCH_MEMBER_PASSWORD));

        LoginDto.Request request = new LoginDto.Request(email, password);
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.NO_MATCH_MEMBER_PASSWORD.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.NO_MATCH_MEMBER_PASSWORD.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 찾기 테스트")
    void findEmail() throws Exception {
        given(memberFindInfoService.findEmail(name, phone)).willReturn(email);

        FindEmailDto.Request request = new FindEmailDto.Request(name, phone);
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/member/find/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andDo(print());

        verify(memberFindInfoService).findEmail(name, phone);
    }

    @Test
    @DisplayName("비밀번호 찾기 테스트")
    void findPassword() throws Exception {
        final String tmpPassword = "tempPassword";
        given(memberUpdateAccountService.updateTemporaryPassword(email, name, phone))
                .willReturn(tmpPassword);

        FindPasswordDto.Request request = new FindPasswordDto.Request(email, name, phone);
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/member/find/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tempPassword").value(tmpPassword))
                .andDo(print());

        verify(memberUpdateAccountService).updateTemporaryPassword(email, name, phone);
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    @WithAuthUser(email = email)
    void changePassword() throws Exception {
        doNothing().when(memberUpdateAccountService)
                .changePassword(any(Long.class), any(String.class));

        String content = objectMapper.writeValueAsString(new ChangePasswordDto("newPassword1@"));

        mockMvc.perform(post("/member/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberUpdateAccountService).changePassword(any(Long.class), any(String.class));
    }

    @Test
    @DisplayName("포인트 찾기 테스트")
    @WithAuthUser(email = email)
    void findPoint() throws Exception {
        final int point = 1234;
        given(memberFindInfoService.findPointByMemberId(any(Long.class)))
                .willReturn(point);

        mockMvc.perform(get("/member/point"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(point))
                .andDo(print());

        verify(memberFindInfoService).findPointByMemberId(any(Long.class));
    }

    @Test
    @DisplayName("로그인 회원 정보 얻기 테스트")
    @WithAuthUser(email = email)
    void findLoginMemberInfo() throws Exception {
        FindMemberInfoByUserIdDto userInfo = new FindMemberInfoByUserIdDto(name, phone, email, birth, MemberRole.BRONZE);
        given(memberFindInfoService.findInfoByUserId(any(Long.class)))
                .willReturn(userInfo);

        mockMvc.perform(get("/member/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.phone").value(phone))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.birth").value(birth))
                .andDo(print());

        verify(memberFindInfoService).findInfoByUserId(any(Long.class));
    }

    @Test
    @DisplayName("로그인 여부 테스트(로그인 완료)")
    @WithAuthUser(email = email)
    void isLoginTrue() throws Exception {
        given(memberFindInfoService.existsMemberById(any(Long.class)))
                .willReturn(true);

        mockMvc.perform(get("/member/isLogin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLogin").value(true))
                .andDo(print());

        verify(memberFindInfoService).existsMemberById(any(Long.class));
    }

    @Test
    @DisplayName("로그인 여부 테스트(로그인 안함)")
    void isLoginFalse() throws Exception {
        mockMvc.perform(get("/member/isLogin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLogin").value(false))
                .andDo(print());
    }
}