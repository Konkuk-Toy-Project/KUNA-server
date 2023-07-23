package konkuk.shop.domain.member.exception;


import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;

public class NothingChangePasswordException extends ApplicationException {

    public NothingChangePasswordException() {
        super(ErrorCode.NOTHING_CHANGE_PASSWORD);
    }
}
