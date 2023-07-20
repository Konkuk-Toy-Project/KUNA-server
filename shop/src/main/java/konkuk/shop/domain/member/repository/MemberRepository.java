package konkuk.shop.domain.member.repository;

import konkuk.shop.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNameAndPhone(String name, String phone);

    Optional<Member> findByEmailAndNameAndPhone(String email, String name, String phone);

    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
}
