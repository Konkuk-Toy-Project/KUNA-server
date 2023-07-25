package konkuk.shop.domain.member.dto;

import konkuk.shop.global.validation.annotation.UserPasswordValid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SignupDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Email
        private String email;

        @UserPasswordValid
        private String password;

        @NotBlank
        private String name;

        @Pattern(regexp = "^[0-9]*$", message = "전화번호가 형식에 맞지 않습니다.")
        @Length(min = 9)
        private String phone;

        @Pattern(regexp = "^[0-9]*$", message = "생년월일이 형식에 맞지 않습니다.")
        @Length(min = 8, max = 8)
        private String birth;

        private String role;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private String role;
        private Long memberId;
    }
}
