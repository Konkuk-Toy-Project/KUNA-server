package konkuk.shop.repository;

import konkuk.shop.entity.AdminMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMemberRepository extends JpaRepository<AdminMember, Long> {
}
