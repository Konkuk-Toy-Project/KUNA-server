package konkuk.shop.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseSignupForm {
    String role;
    Long memberId;
}
