package konkuk.shop.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SignupDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String email;
        private String password;
        private String name;
        private String phone;
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
