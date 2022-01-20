package konkuk.shop.repository;

import konkuk.shop.entity.CategoryItem;
import konkuk.shop.entity.DetailImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long> {
}
