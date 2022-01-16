package konkuk.shop.form.requestForm;

import lombok.Data;

@Data
public class RequestSignupForm {
    String email;
    String password;
    String name;
    String phone;
    String birth;
}
