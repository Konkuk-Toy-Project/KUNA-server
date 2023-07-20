package konkuk.shop.domain.image.repository;

import konkuk.shop.domain.image.entity.DetailImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailImageRepository extends JpaRepository<DetailImage, Long> {
}
