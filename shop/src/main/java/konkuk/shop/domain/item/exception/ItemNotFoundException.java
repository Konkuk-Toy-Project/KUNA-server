package konkuk.shop.domain.item.exception;

import konkuk.shop.global.exception.ErrorCode;
import konkuk.shop.global.exception.NotFoundException;

public class ItemNotFoundException extends NotFoundException {

    public ItemNotFoundException() {
        super(ErrorCode.NO_FIND_ITEM);
    }
}
