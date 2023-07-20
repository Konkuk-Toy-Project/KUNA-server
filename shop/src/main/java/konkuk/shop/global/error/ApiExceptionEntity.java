package konkuk.shop.global.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiExceptionEntity {
    private String errorCode;
    private String message;
}
