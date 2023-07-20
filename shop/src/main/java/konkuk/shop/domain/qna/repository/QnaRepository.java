package konkuk.shop.domain.qna.repository;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.qna.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {
    List<Qna> findAllByItemId(Long itemId);

    List<Qna> findAllByAdminMember(AdminMember adminMember);
}
