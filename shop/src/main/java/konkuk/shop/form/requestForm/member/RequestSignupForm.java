package konkuk.shop.form.requestForm.member;

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
