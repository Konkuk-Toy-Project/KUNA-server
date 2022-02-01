package konkuk.shop.repository;

import konkuk.shop.entity.Option1;
import konkuk.shop.entity.Option2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Option2Repository extends JpaRepository<Option2, Long> {
    Optional<Option2> findByOption1(Option1 option1);
   boolean existsByOption1(Option1 option1);
}
