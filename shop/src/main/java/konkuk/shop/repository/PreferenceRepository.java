package konkuk.shop.repository;

import konkuk.shop.entity.PreferenceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceRepository extends JpaRepository<PreferenceItem, Long> {
    boolean existsByMemberIdAndItemId(Long userId, Long itemId);
}
