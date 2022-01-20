package konkuk.shop.repository;

import konkuk.shop.entity.DetailImage;
import konkuk.shop.entity.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
}
