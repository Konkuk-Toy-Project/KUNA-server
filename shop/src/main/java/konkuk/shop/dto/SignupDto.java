package konkuk.shop.dto;

import lombok.Data;

@Data
public class SignupDto {
    String email;
    String password;
    String name;
    String phone;
    String birth;
}
