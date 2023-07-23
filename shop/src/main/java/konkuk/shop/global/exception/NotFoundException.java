package konkuk.shop.global.exception;

public class NotFoundException extends ApplicationException {

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }

    public NotFoundException(ErrorCode e) {
        super(e);
    }
}