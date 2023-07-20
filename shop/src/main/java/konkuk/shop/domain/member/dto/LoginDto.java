package konkuk.shop.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class LoginDto {

    @Getter
    public static class Request {
        private String email;
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private String token;
        private String role;
    }
}
