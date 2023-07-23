package konkuk.shop.domain.member.application;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.member.exception.AdminNotFoundException;
import konkuk.shop.domain.member.exception.UserNotFoundException;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.dto.FindMemberInfoByUserIdDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFindInfoService {
    private final MemberRepository memberRepository;
    private final AdminMemberRepository adminMemberRepository;

    public boolean isDuplicateEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean existsMemberById(Long id) {
        return memberRepository.existsById(id);
    }

    public String findEmail(String name, String phone) {
        return memberRepository.findByNameAndPhone(name, phone)
                .orElseThrow(UserNotFoundException::new)
                .getEmail();
    }

    public AdminMember findAdminByMemberId(Long userId) {
        return adminMemberRepository.findByMemberId(userId)
                .orElseThrow(AdminNotFoundException::new);
    }

    public Integer findPointByMemberId(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new)
                .getPoint();
    }

    public FindMemberInfoByUserIdDto findInfoByUserId(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return FindMemberInfoByUserIdDto.of(member);
    }
}
