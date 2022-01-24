package konkuk.shop.repository;

import konkuk.shop.entity.AdminMember;
import konkuk.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminMemberRepository extends JpaRepository<AdminMember, Long> {
    Optional<AdminMember> findByMember(Member member);
}
