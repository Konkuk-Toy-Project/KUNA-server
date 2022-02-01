package konkuk.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class FindReviewDto {
    String memberName;
    String option;
    String description;
    Integer rate;
    LocalDateTime registryDate;
    List<String> reviewImagesUrl;
}
