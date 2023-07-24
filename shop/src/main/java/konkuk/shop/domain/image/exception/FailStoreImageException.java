package konkuk.shop.domain.image.exception;


import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;

public class FailStoreImageException extends ApplicationException {

    public FailStoreImageException() {
        super(ErrorCode.FAIL_STORE_IMAGE);
    }
}
