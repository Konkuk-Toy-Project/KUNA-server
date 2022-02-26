package konkuk.shop.form.requestForm.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFindPasswordForm {
    String email;
    String name;
    String phone;
}
