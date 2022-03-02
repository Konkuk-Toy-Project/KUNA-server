package konkuk.shop.service;

import konkuk.shop.dto.IsPreference;
import konkuk.shop.dto.PreferenceDto;
import konkuk.shop.entity.Item;
import konkuk.shop.entity.Member;
import konkuk.shop.entity.PreferenceItem;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.repository.ItemRepository;
import konkuk.shop.repository.MemberRepository;
import konkuk.shop.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));
        PreferenceItem savePreferenceItem = preferenceRepository.save(new PreferenceItem(member, item));

        member.getPreferenceItems().add(savePreferenceItem);
        item.plusPreferenceCount();
        return savePreferenceItem.getId();
    }

    public List<PreferenceDto> findPreferenceByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
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
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_PREFERENCE));

        if (!preferenceItem.getMember().getId().equals(memberId))
            throw new ApiException(ExceptionEnum.NOT_AUTHORITY_PREFERENCE_EDIT);
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
