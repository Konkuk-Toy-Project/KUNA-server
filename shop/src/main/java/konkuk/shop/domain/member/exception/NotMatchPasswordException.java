package konkuk.shop.domain.member.exception;


import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;

public class NotMatchPasswordException extends ApplicationException {

    public NotMatchPasswordException() {
        super(ErrorCode.NO_MATCH_MEMBER_PASSWORD);
    }
}
