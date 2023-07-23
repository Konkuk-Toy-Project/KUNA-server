package konkuk.shop.domain.member.exception;


import konkuk.shop.global.exception.ErrorCode;
import konkuk.shop.global.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super(ErrorCode.NO_FIND_MEMBER_EMAIL);
    }
}
