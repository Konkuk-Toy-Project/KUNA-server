package konkuk.shop.domain.admin.repository;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminMemberRepository extends JpaRepository<AdminMember, Long> {
    Optional<AdminMember> findByMember(Member member);

    Optional<AdminMember> findByMemberId(Long userId);

    boolean existsByMember(Member findMember);
}
