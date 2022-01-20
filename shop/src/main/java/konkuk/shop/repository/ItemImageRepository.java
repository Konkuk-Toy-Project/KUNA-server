package konkuk.shop.repository;

import konkuk.shop.entity.DetailImage;
import konkuk.shop.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
}
