package konkuk.shop.repository;

import konkuk.shop.entity.Category;
import konkuk.shop.entity.DetailImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
