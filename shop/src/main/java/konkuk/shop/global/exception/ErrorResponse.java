package konkuk.shop.global.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private String errorCode;
    private String message;

    public static ResponseEntity<?> toResponseEntity(final ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    public static ResponseEntity<?> toResponseEntity(final ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, message));
    }

    public static ErrorResponse of(final ErrorCode errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static ErrorResponse of(final ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .errorCode(errorCode.getCode())
                .message(message)
                .build();
    }
}
