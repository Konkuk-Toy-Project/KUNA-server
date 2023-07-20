package konkuk.shop.service;

import konkuk.shop.domain.category.application.CategoryService;
import konkuk.shop.dto.FindAllCategoryDto;
import konkuk.shop.domain.category.entity.Category;
import konkuk.shop.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class CategoryServiceTest {
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryService categoryService;

    private final Long categoryId = 10L;

    @Test
    @DisplayName("카테고리 찾기 테스트")
    void findCategoryById() {
        //given
        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(new Category("카테고리")));

        //when
        Category category = categoryService.findCategoryById(categoryId);

        //then
        assertThat(category.getName()).isEqualTo("카테고리");
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    @DisplayName("카테고리 추가 테스트")
    void addCategory() {
        //given
        given(categoryRepository.save(any(Category.class))).willReturn(new Category("카테고리"));

        //when
        Category category = categoryService.addCategory("카테고리");

        //then
        assertThat(category.getName()).isEqualTo("카테고리");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("카테고리 목록 요청 테스트")
    void findAll() {
        //given
        given(categoryRepository.findAll()).willReturn(new ArrayList<>());

        //when
        List<FindAllCategoryDto> result = categoryService.findAll();

        //then
        assertThat(result).isEmpty();
        verify(categoryRepository).findAll();
    }
}