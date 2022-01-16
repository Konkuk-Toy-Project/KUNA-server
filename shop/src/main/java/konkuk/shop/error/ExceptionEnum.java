package konkuk.shop.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0001", "예상치 못한 서버 에러입니다."),
    DUPLICATION_MEMBER_EMAIL(HttpStatus.BAD_REQUEST, "M002", "중복된 이메일입니다."),
    NO_FIND_MEMBER_EMAIL(HttpStatus.BAD_REQUEST, "M003", "존재하지 않은 이메일입니다."),
    NO_MATCH_MEMBER_PASSWORD(HttpStatus.BAD_REQUEST, "M004", "이메일과 비밀번호가 일치하지 않습니다."),
    NO_FIND_MEMBER(HttpStatus.BAD_REQUEST, "M005", "존재하지 않은 회원 정보입니다.");

    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
