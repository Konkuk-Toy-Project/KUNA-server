package konkuk.shop.domain.member.entity;

import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;

public enum MemberRole {
    ADMIN, BRONZE, SILVER, GOLD;

    public static MemberRole convertToMemberRole(String role) {
        if (role.equals("user")) {
            return MemberRole.BRONZE;
        }
        else if (role.equals("admin")) {
            return MemberRole.ADMIN;
        }
        else {
            throw new ApplicationException(ErrorCode.NOT_FIND_ROLE);
        }
    }
}
