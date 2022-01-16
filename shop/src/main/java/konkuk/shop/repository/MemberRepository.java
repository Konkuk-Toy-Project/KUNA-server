package konkuk.shop.repository;

import konkuk.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNameAndPhone(String name, String phone);

    Optional<Member> findByEmailAndNameAndPhone(String email, String name, String phone);
}
