package konkuk.shop.domain.category.api;

import konkuk.shop.dto.FindAllCategoryDto;
import konkuk.shop.domain.category.application.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 서비스 정책에 따라 카테고리 추가 및 수정도 가능
     */

    @GetMapping
    public ResponseEntity<List<FindAllCategoryDto>> findAllCategory() {
        List<FindAllCategoryDto> result = categoryService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
