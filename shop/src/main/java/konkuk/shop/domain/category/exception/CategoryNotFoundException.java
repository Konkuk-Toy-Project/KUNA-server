package konkuk.shop.domain.category.exception;


import konkuk.shop.global.exception.ErrorCode;
import konkuk.shop.global.exception.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException() {
        super(ErrorCode.NO_FIND_CATEGORY);
    }
}
