package konkuk.shop.domain.item.repository;

import konkuk.shop.domain.item.entity.Option1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Option1Repository extends JpaRepository<Option1, Long> {
}
