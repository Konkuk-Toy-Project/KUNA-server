package konkuk.shop.dto;

import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FindMemberInfoByUserIdDto {
    String name;
    String phone;
    String email;
    String birth;
    MemberRole role;

    public static FindMemberInfoByUserIdDto of(Member member) {
        return FindMemberInfoByUserIdDto.builder()
                .birth(member.getBirth())
                .name(member.getName())
                .phone(member.getPhone())
                .role(member.getMemberRole())
                .email(member.getEmail())
                .build();
    }
}
