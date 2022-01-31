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

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    //private final CategoryItemRepository categoryItemRepository;

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CATEGORY));
    }

    public Category addCategory(String name){
        return categoryRepository.save(new Category(name));
    }

    public List<FindAllCategoryDto> findAll() {
        List<Category> categories = categoryRepository.findAll();

        List<FindAllCategoryDto> result = new ArrayList<>();
        for (Category category : categories) {
            result.add(new FindAllCategoryDto(category.getId(), category.getName()));
        }
        return result;
    }
}
