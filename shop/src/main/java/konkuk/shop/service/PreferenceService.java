package konkuk.shop.service;

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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreferenceService {
    private final PreferenceRepository preferenceRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public Long savePreferenceItem(Long memberId, Long itemId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));
        PreferenceItem savePreferenceItem = preferenceRepository.save(new PreferenceItem(member, item));
        member.getPreferenceItems().add(savePreferenceItem);
        return savePreferenceItem.getId();
    }

    public List<PreferenceDto> findPreferenceByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        List<PreferenceItem> preferenceItems = member.getPreferenceItems();
        List<PreferenceDto> result = new ArrayList<>();

        for (PreferenceItem preferenceItem : preferenceItems) {
            Item item = preferenceItem.getItem();
            result.add(new PreferenceDto(item.getThumbnail().getStore_name(),
                    item.getName(), item.getPrice(), item.getSale(), preferenceItem.getId()));
        }

        return result;
    }

    public void deletePreference(Long memberId, Long preferenceId) {
        PreferenceItem preferenceItem = preferenceRepository.findById(preferenceId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_PREFERENCE));

        if(preferenceItem.getMember().getId() != memberId) throw new ApiException(ExceptionEnum.NOT_AUTHORITY_PREFERENCE_EDIT);
        preferenceRepository.deleteById(preferenceId);
    }
}
