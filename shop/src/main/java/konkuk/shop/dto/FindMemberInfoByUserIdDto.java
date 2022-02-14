package konkuk.shop.dto;

import konkuk.shop.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FindMemberInfoByUserIdDto {
    String name;
    String phone;
    String email;
    String birth;
    MemberRole role;
}
