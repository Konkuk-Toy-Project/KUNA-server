package konkuk.shop.domain.item.exception;

import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;

public class NoAuthorityAccessItem extends ApplicationException {

    public NoAuthorityAccessItem() {
        super(ErrorCode.NO_AUTHORITY_ACCESS_ITEM);
    }
}
