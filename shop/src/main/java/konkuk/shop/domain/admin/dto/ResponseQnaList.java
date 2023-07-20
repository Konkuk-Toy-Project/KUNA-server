package konkuk.shop.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ResponseQnaList {
    String itemName;
    Long itemId;
    Long qnaId;
    boolean isSecret;
    boolean isAnswered;
    String question;
    LocalDateTime registryDate;
    String memberName;
    String answer;
    String title;
}
