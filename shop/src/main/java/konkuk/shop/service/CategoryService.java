package konkuk.shop.service;

import konkuk.shop.dto.FindAllCategoryDto;
import konkuk.shop.entity.Category;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CATEGORY));
    }

    public Category addCategory(String name) {
        return categoryRepository.save(new Category(name));
    }

    public List<FindAllCategoryDto> findAll() {
        List<FindAllCategoryDto> result = categoryRepository.findAll()
                .stream()
                .map(e -> new FindAllCategoryDto(e.getId(), e.getName()))
                .collect(Collectors.toList());

        log.info("카테고리 목록 요청");
        return result;
    }
}
