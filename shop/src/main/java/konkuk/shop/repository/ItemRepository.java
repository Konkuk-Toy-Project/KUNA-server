package konkuk.shop.repository;

import konkuk.shop.entity.AdminMember;
import konkuk.shop.entity.Category;
import konkuk.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByCategory(Category category);

    List<Item> findByAdminMember(AdminMember adminMember);
}
