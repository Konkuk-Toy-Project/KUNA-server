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
    NOT_FIND_ROLE(HttpStatus.BAD_REQUEST, "M008", "Role을 찾을 수 없습니다."),
    DUPLICATION_MEMBER_PHONE(HttpStatus.BAD_REQUEST, "M009", "중복된 전화번호입니다."),
    NOT_EMAIL_FORM(HttpStatus.BAD_REQUEST, "M010", "이메일 형식이 틀립니다."),
    NOT_PASSWORD_FORM(HttpStatus.BAD_REQUEST, "M011", "비밀번호는 영문과 특수문자, 숫자를 포함하며 8자 이상이어야 합니다."),
    NOT_NAME_FORM(HttpStatus.BAD_REQUEST, "M011", "이름에 공백이 있으면 안됩니다."),
    NOT_PHONE_FORM(HttpStatus.BAD_REQUEST, "M011", "전화번호가 형식에 맞지 않습니다."),
    NOT_BIRTH_FORM(HttpStatus.BAD_REQUEST, "M011", "생년월일이 형식에 맞지 않습니다."),

    NO_FIND_ITEM_BY_ID(HttpStatus.BAD_REQUEST, "I001", "존재하지 않는 아이템입니다."),
    NO_STOCK_ITEM(HttpStatus.BAD_REQUEST, "I002", "해당 옵션의 재고가 부족합니다."),
    NO_AUTHORITY_ACCESS_ITEM(HttpStatus.BAD_REQUEST, "Q001", "해당 상품을 수정할 권한이 없습니다."),

    NO_FIND_OPTION1_BY_ID(HttpStatus.BAD_REQUEST, "P001", "존재하지 않는 옵션1입니다."),
    NO_FIND_OPTION2_BY_ID(HttpStatus.BAD_REQUEST, "P002", "존재하지 않는 옵션2입니다."),
    NO_MATCH_OPTION1_WITH_ITEM(HttpStatus.BAD_REQUEST, "P003", "해당 상품에는 없는 옵션1입니다."),
    NO_MATCH_OPTION2_WITH_OPTION1(HttpStatus.BAD_REQUEST, "P004", "해당 옵션1에는 없는 옵션2입니다."),
    NECESSARY_OPTION2(HttpStatus.BAD_REQUEST, "P005", "해당 상품은 옵션2가 반드시 있어야합니다."),

    NO_FIND_THUMBNAIL_BY_ID(HttpStatus.BAD_REQUEST, "T001", "존재하지 않는 썸네일입니다."),
    FAIL_STORE_IMAGE(HttpStatus.BAD_REQUEST, "T002", "이미지 저장 실패"),
    FAIL_CALL_IMAGE(HttpStatus.BAD_REQUEST, "T003", "이미지 불러오기 실패"),

    NO_FIND_COUPON(HttpStatus.BAD_REQUEST, "C001", "존재하지 않는 쿠폰입니다."),
    ALREADY_REGISTRY_COUPON(HttpStatus.BAD_REQUEST, "C002", "이미 등록된 쿠폰입니다."),
    NOT_SATISFY_USE_COUPON(HttpStatus.BAD_REQUEST, "C003", "쿠폰 사용 조건을 만족하지 못합니다."),
    NOT_MATCH_COUPON_MEMBER(HttpStatus.BAD_REQUEST, "C004", "해당 쿠폰 소유자가 아닙니다."),
    ALREADY_USED_COUPON(HttpStatus.BAD_REQUEST, "C005", "이미 사용한 쿠폰입니다."),
    EXPIRED_COUPON(HttpStatus.BAD_REQUEST, "C006", "쿠폰 사용기한이 지났습니다."),

    NO_FIND_CATEGORY(HttpStatus.BAD_REQUEST, "CT01", "존재하지 않는 카테고리입니다."),

    NO_FIND_CART_ITEM(HttpStatus.BAD_REQUEST, "CR01", "존재하지 않는 장바구니 아이템입니다."),
    NOT_AUTHORITY_CART_EDIT(HttpStatus.BAD_REQUEST, "CR02", "해당 장바구니를 수정할 권한이 없습니다."),

    NO_FIND_PREFERENCE(HttpStatus.BAD_REQUEST, "P001", "해당 찜하기를 찾을 수 없습니다."),
    NOT_AUTHORITY_PREFERENCE_EDIT(HttpStatus.BAD_REQUEST, "P002", "해당 찜하기를 수정할 권한이 없습니다."),

    NO_FIND_ORDER(HttpStatus.BAD_REQUEST, "O001", "해당 주문을 찾을 수 없습니다."),
    INCORRECT_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, "O002", "옯바르지 않는 결제 방식입니다."),
    INCORRECT_SHIPPING_CHARGE(HttpStatus.BAD_REQUEST, "O003", "옯바르지 않는 배송금액입니다."),
    INCORRECT_TOTAL_PRICE(HttpStatus.BAD_REQUEST, "O004", "합계 금액이 맞지 않습니다."),
    NO_AUTHORITY_ACCESS_ORDER(HttpStatus.BAD_REQUEST, "O005", "해당 주문의 접근할 권한이 없습니다."),
    NO_FIND_ORDER_ITEM(HttpStatus.BAD_REQUEST, "O006", "해당 주문 상품을 찾을 수 없습니다."),

    NO_FIND_ADMIN_MEMBER(HttpStatus.BAD_REQUEST, "A001", "해당 관리자를 찾을 수 없습니다."),

    NO_FIND_QNA(HttpStatus.BAD_REQUEST, "Q001", "해당 Qna를 찾을 수 없습니다."),
    NO_AUTHORITY_ANSWER_QNA(HttpStatus.BAD_REQUEST, "Q002", "해당 Qna에 답변할 권한이 없습니다."),
    ALREADY_ANSWER_QNA(HttpStatus.BAD_REQUEST, "Q003", "이미 해당 Qna에 답변을 했습니다."),

    NO_FIND_REVIEW(HttpStatus.BAD_REQUEST, "R001", "해당 리뷰를 찾을 수 없습니다."),
    ALREADY_REGISTRY_REVIEW(HttpStatus.BAD_REQUEST, "R002", "이미 리뷰를 작성했습니다."),

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
