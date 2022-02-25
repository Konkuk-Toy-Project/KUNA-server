package konkuk.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import konkuk.shop.dto.SignupDto;
import konkuk.shop.form.requestForm.member.RequestSignupForm;
import konkuk.shop.security.TokenProvider;
import konkuk.shop.service.MemberService;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest() : 매개변수가 없으면 @Controller와 @RestController, @ControllerAdvice 등 WEb과 관련된 설정된 클래스를 찾아 메모리에 생성한다.
@WebMvcTest(MemberController.class) // 서블릿 컨테이너를 모킹한다. 컨트롤러를 테스트할 때 사용(DispatcherSeravlet이 필요하기 때문)
class MemberControllerTest {
    @MockBean
    MemberService memberService;

    @MockBean
    TokenProvider tokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("이메일 중복 테스트")
    void isDuplicationEmail() throws Exception {
        //given
        given(memberService.isDuplicateEmail("asdf@asdf.com")).willReturn(true);
        JSONObject json = new JSONObject();
        json.put("email", "asdf@asdf.com");
        String content = json.toString();

        //when
        mockMvc.perform(
                    post("/member/duplication/email")
                            .content("{\"email\": \"asdf@asdf.com\"}")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDuplication").exists())
                .andDo(print());

        verify(memberService).isDuplicateEmail("asdf@asdf.com");

        //then
    }

    @Test
    @DisplayName("회원가입 테스트")
    void memberSignup() throws Exception {
        //given
        given(memberService.signup(
                new SignupDto("asdf@asdf.com", "asdf", "test", "01012345678", "19991023", "user"))
        ).willReturn(1234L);

        RequestSignupForm form = new RequestSignupForm("asdf@asdf.com", "asdf", "test", "01012345678", "19991023", "user");

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

        //when

        //then
        verify(memberService)
                .signup(new SignupDto("asdf@asdf.com", "asdf", "test", "01012345678", "19991023", "user"));
    }
}