package konkuk.shop.domain.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LoginDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
