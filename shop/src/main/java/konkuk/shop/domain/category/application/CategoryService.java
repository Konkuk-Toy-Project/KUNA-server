package konkuk.shop.domain.category.application;

import konkuk.shop.dto.FindAllCategoryDto;
import konkuk.shop.domain.category.entity.Category;
import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;
import konkuk.shop.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_CATEGORY));
    }

    public Category addCategory(String name) {
        return categoryRepository.save(new Category(name));
    }

    public List<FindAllCategoryDto> findAll() {
        log.info("카테고리 목록 요청");
        return categoryRepository.findAll()
                .stream()
                .map(e -> new FindAllCategoryDto(e.getId(), e.getName()))
                .collect(Collectors.toList());

    }
}
