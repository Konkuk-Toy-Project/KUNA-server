package konkuk.shop.domain.item.repository;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByAdminMember(AdminMember adminMember);

    List<Item> findByCategoryId(Long categoryId);
}
