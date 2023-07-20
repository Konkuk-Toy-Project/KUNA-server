package konkuk.shop.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginCheckDto {
    @JsonProperty("isLogin")
    private boolean login;
}
