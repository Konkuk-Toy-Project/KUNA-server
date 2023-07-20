package konkuk.shop.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSignupForm {
    String email;
    String password;
    String name;
    String phone;
    String birth;
    String role;
}
