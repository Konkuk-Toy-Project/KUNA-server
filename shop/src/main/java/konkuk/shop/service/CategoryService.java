package konkuk.shop.service;


import konkuk.shop.entity.CategoryItem;
import konkuk.shop.repository.CategoryItemRepository;
import konkuk.shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryItemRepository categoryItemRepository;


}
