package konkuk.shop.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class DuplicationEmailDto {

    @Getter
    public static class Request {
        private String email;
    }

    @AllArgsConstructor
    @Getter
    public static class Response {
        @JsonProperty("isDuplication")
        private boolean duplication;
    }
}
