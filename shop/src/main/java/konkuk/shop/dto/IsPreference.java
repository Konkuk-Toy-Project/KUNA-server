package konkuk.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IsPreference {
    boolean isPreference;
    boolean isLogin;
    Long preferenceId;
}
