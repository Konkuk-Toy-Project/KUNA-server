package konkuk.shop.domain.preference.repository;

import konkuk.shop.domain.preference.entity.PreferenceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<PreferenceItem, Long> {
    boolean existsByMemberIdAndItemId(Long userId, Long itemId);

    Optional<PreferenceItem> findByMemberIdAndItemId(Long userId, Long itemId);
}
