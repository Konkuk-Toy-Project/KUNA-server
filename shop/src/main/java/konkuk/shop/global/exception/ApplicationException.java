package konkuk.shop.global.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorCode error;

    public ApplicationException(ErrorCode e) {
        super(e.getMessage());
        this.error = e;
    }
}