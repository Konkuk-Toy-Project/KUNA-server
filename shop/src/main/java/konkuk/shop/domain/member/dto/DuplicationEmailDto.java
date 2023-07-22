package konkuk.shop.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DuplicationEmailDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Request {
        private String email;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        @JsonProperty("isDuplication")
        private boolean duplication;
    }
}
