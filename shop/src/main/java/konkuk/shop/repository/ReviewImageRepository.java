package konkuk.shop.repository;

import konkuk.shop.entity.DetailImage;
import konkuk.shop.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
