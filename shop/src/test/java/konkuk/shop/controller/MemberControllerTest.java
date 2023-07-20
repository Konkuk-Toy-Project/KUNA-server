package konkuk.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import konkuk.shop.WithAuthUser;
import konkuk.shop.domain.member.api.MemberController;
import konkuk.shop.domain.member.application.MemberService;
import konkuk.shop.domain.member.dto.LoginDto;
import konkuk.shop.domain.member.dto.ChangePasswordDto;
import konkuk.shop.domain.member.dto.FindEmailDto;
import konkuk.shop.domain.member.dto.FindPasswordDto;
import konkuk.shop.domain.member.dto.SignupDto;
import konkuk.shop.domain.member.entity.MemberRole;
import konkuk.shop.dto.FindMemberInfoByUserIdDto;
import konkuk.shop.global.security.TokenProvider;
import org.json.JSONObject;
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

// @WebMvcTest() : 매개변수가 없으면 @Controller와 @RestController, @ControllerAdvice 등 WEb과 관련된 설정된 클래스를 찾아 메모리에 생성한다.
@WebMvcTest(MemberController.class) // 서블릿 컨테이너를 모킹한다. 컨트롤러를 테스트할 때 사용(DispatcherSeravlet이 필요하기 때문)
class MemberControllerTest {
    private final String name = "testMember";
    private final String email = "asdf@asdf.com";
    private final String password = "asdfasdf@1";
    private final String phone = "01012345678";
    private final String birth = "20000327";
    private final String role = "user";

    @MockBean
    MemberService memberService;
    @MockBean
    TokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("이메일 중복 테스트")
    void isDuplicationEmail() throws Exception {
        given(memberService.isDuplicateEmail(email)).willReturn(true);

        JSONObject json = new JSONObject();
        json.put("email", email);
        String content = json.toString();

        mockMvc.perform(
                        post("/member/duplication/email")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDuplication").exists())
                .andDo(print());

        verify(memberService).isDuplicateEmail(email);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void memberSignup() throws Exception {
        given(memberService.signup(
                new SignupDto.Request(email, password, name, phone, birth, role))
        ).willReturn(1234L);

        SignupDto.Request form = new SignupDto.Request(email, password, name, phone, birth, role);
        Gson gson = new Gson();
        String content = gson.toJson(form);
        //또는 아래와 같이 json변환 가능
        //String json = new ObjectMapper().writeValueAsString(form);

        mockMvc.perform(
                        post("/member/signup")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").exists())
                .andExpect(jsonPath("$.memberId").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 테스트")
    void login() throws Exception {
        given(memberService.login(email, password)).willReturn(new LoginDto.Response("JWTToken", role));

        LoginDto.Response form = new LoginDto.Response(email, password);
        String content = new ObjectMapper().writeValueAsString(form);

        mockMvc.perform(
                        post("/member/login")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").exists())
                .andDo(print());

        verify(memberService).login(email, password);
    }

    @Test
    @DisplayName("이메일 찾기 테스트")
    void findEmail() throws Exception {
        given(memberService.findEmail(name, phone)).willReturn(email);

        FindEmailDto.Request form = new FindEmailDto.Request(name, phone);
        String content = new ObjectMapper().writeValueAsString(form);

        mockMvc.perform(
                        post("/member/find/email")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andDo(print());

        verify(memberService).findEmail(name, phone);
    }

    @Test
    @DisplayName("비밀번호 찾기 테스트")
    void findPassword() throws Exception {
        given(memberService.findPassword(email, name, phone)).willReturn("tempPassword");

        FindPasswordDto.Request form = new FindPasswordDto.Request(email, name, phone);
        String content = new ObjectMapper().writeValueAsString(form);

        mockMvc.perform(
                        post("/member/find/password")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tempPassword").value("tempPassword"))
                .andDo(print());

        verify(memberService).findPassword(email, name, phone);
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    @WithAuthUser(email = email)
    void changePassword() throws Exception {
        doNothing().when(memberService).changePassword(any(Long.class), any(String.class));
        String content = new ObjectMapper()
                .writeValueAsString(new ChangePasswordDto("changePassword"));

        mockMvc.perform(
                        post("/member/change/password")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).changePassword(any(Long.class), any(String.class));
    }

    @Test
    @DisplayName("포인트 찾기 테스트")
    @WithAuthUser(email = email)
    void findPoint() throws Exception {
        given(memberService.findPointByMemberId(any(Long.class))).willReturn(1234);

        mockMvc.perform(get("/member/point"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(1234))
                .andDo(print());

        verify(memberService).findPointByMemberId(any(Long.class));
    }

    @Test
    @DisplayName("로그인 회원 정보 얻기 테스트")
    @WithAuthUser(email = email)
    void findLoginMemberInfo() throws Exception {
        given(memberService.findInfoByUserId(any(Long.class)))
                .willReturn(new FindMemberInfoByUserIdDto(name, phone, email, birth, MemberRole.BRONZE));

        mockMvc.perform(get("/member/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.phone").value(phone))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.birth").value(birth))
                .andDo(print());

        verify(memberService).findInfoByUserId(any(Long.class));
    }

    @Test
    @DisplayName("로그인 여부 테스트(로그인 완료)")
    @WithAuthUser(email = email)
    void isLoginTrue() throws Exception {
        given(memberService.existsMemberById(any(Long.class)))
                .willReturn(true);

        mockMvc.perform(get("/member/isLogin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLogin").value(true))
                .andDo(print());

        verify(memberService).existsMemberById(any(Long.class));
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