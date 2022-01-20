package konkuk.shop.repository;

import konkuk.shop.entity.DetailImage;
import konkuk.shop.entity.Option1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailImageRepository extends JpaRepository<DetailImage, Long> {
}
