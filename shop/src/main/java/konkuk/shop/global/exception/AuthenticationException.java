package konkuk.shop.global.exception;

public class AuthenticationException extends ApplicationException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

}
