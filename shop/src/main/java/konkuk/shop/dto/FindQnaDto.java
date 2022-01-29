package konkuk.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class FindQnaDto {
    boolean isSecret;
    boolean isAnswered;
    String question;
    String answer;
    LocalDateTime registryDate;
    String memberName;
}
