package konkuk.shop.domain.member.dto;

import konkuk.shop.global.validation.annotation.UserPasswordValid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangePasswordDto {
    @UserPasswordValid
    private String newPassword;
}
