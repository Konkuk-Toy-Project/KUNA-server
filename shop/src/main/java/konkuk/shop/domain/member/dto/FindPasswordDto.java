package konkuk.shop.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class FindPasswordDto {

    @Getter
    @AllArgsConstructor
    public static class Request {
        private String email;
        private String name;
        private String phone;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private String tempPassword;
    }

}
