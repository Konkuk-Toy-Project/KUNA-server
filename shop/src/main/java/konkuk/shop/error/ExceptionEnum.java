package konkuk.shop.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0001", "예상치 못한 서버 에러입니다."),

    DUPLICATION_MEMBER_EMAIL(HttpStatus.BAD_REQUEST, "M002", "중복된 이메일입니다."),
    NO_FIND_MEMBER_EMAIL(HttpStatus.BAD_REQUEST, "M003", "존재하지 않은 이메일입니다."),
    NO_MATCH_MEMBER_PASSWORD(HttpStatus.BAD_REQUEST, "M004", "이메일과 비밀번호가 일치하지 않습니다."),
    NO_FIND_MEMBER(HttpStatus.BAD_REQUEST, "M005", "존재하지 않은 회원 정보입니다."),
    NOTHING_CHANGE_PASSWORD(HttpStatus.BAD_REQUEST, "M006", "전에 사용하던 비밀번호와 일치합니다."),
    NOT_ADMIN_MEMBER(HttpStatus.BAD_REQUEST, "M007", "현재 admin 회원이 아닙니다."),

    NO_FIND_ITEM_BY_ID(HttpStatus.BAD_REQUEST, "I001", "존재하지 않는 아이템입니다."),

    NO_FIND_OPTION1_BY_ID(HttpStatus.BAD_REQUEST, "P001", "존재하지 않는 옵션1입니다."),
    NO_FIND_OPTION2_BY_ID(HttpStatus.BAD_REQUEST, "P002", "존재하지 않는 옵션2입니다."),

    NO_FIND_THUMBNAIL_BY_ID(HttpStatus.BAD_REQUEST, "T001", "존재하지 않는 이미지입니다."),
    FAIL_STORE_IMAGE(HttpStatus.BAD_REQUEST, "T002", "이미지 저장 실패"),

    NO_FIND_COUPON(HttpStatus.BAD_REQUEST, "C001", "존재하지 않는 쿠폰입니다."),

    NO_FIND_CATEGORY(HttpStatus.BAD_REQUEST, "CT01", "존재하지 않는 카테고리입니다."),

    NO_FIND_CART_ITEM(HttpStatus.BAD_REQUEST, "CR01", "존재하지 않는 장바구니 아이템입니다."),
    NOT_AUTHORITY_CART_EDIT(HttpStatus.BAD_REQUEST, "CR02", "해당 장바구니를 수정할 권한이 없습니다."),

    NO_FIND_PREFERENCE(HttpStatus.BAD_REQUEST, "P001", "해당 찜하기를 찾을 수 없습니다."),
    NOT_AUTHORITY_PREFERENCE_EDIT(HttpStatus.BAD_REQUEST, "P002", "해당 찜하기를 수정할 권한이 없습니다."),

    ;
    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
