package konkuk.shop.form.responseForm.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseSignupForm {
    boolean result;
    Long memberId;
}
