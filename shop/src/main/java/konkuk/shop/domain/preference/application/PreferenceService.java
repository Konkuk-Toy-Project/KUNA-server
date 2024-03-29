package konkuk.shop.domain.preference.application;

import konkuk.shop.dto.IsPreference;
import konkuk.shop.dto.PreferenceDto;
import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.preference.entity.PreferenceItem;
import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;
import konkuk.shop.domain.item.repository.ItemRepository;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.domain.preference.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreferenceService {
    private final PreferenceRepository preferenceRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long savePreferenceItem(Long memberId, Long itemId) {
        log.info("찜하기 요청 memberId={}, itemId={}", memberId, itemId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_MEMBER));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_ITEM));
        PreferenceItem savePreferenceItem = preferenceRepository.save(new PreferenceItem(member, item));

        member.getPreferenceItems().add(savePreferenceItem);
        item.plusPreferenceCount();
        return savePreferenceItem.getId();
    }

    public List<PreferenceDto> findPreferenceByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_MEMBER));
        log.info("찜목록 보기. memberId={}", memberId);

        return member.getPreferenceItems().stream()
                .map(e -> {
                    Item item = e.getItem();
                    return new PreferenceDto(item.getThumbnail().getStore_name(),
                            item.getName(), item.getPrice(), item.getSale(), e.getId());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePreference(Long memberId, Long preferenceId) {
        PreferenceItem preferenceItem = preferenceRepository.findById(preferenceId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_PREFERENCE));

        if (!preferenceItem.getMember().getId().equals(memberId))
            throw new ApplicationException(ErrorCode.NOT_AUTHORITY_PREFERENCE_EDIT);
        preferenceItem.getItem().minusPreferenceCount();

        log.info("찜하기 삭제 요청. memberId={}, preferenceId={}", memberId, preferenceId);

        preferenceRepository.delete(preferenceItem);
    }

    public IsPreference isPreference(Long userId, Long itemId) {
        log.info("isPreference. userId={}, itemId={}", userId, itemId);

        return preferenceRepository.findByMemberIdAndItemId(userId, itemId)
                .map(preferenceItem -> new IsPreference(true, true, preferenceItem.getId()))
                .orElseGet(() -> new IsPreference(false, true, null));

    }
}
