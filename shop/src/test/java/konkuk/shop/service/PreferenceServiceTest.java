package konkuk.shop.service;

import konkuk.shop.domain.preference.application.PreferenceService;
import konkuk.shop.dto.IsPreference;
import konkuk.shop.dto.PreferenceDto;
import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.preference.entity.PreferenceItem;
import konkuk.shop.domain.item.repository.ItemRepository;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.domain.preference.repository.PreferenceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class PreferenceServiceTest {
    private final String name = "testMember";
    private final String email = "asdf@asdf.com";
    private final String password = "asdfasdf@1";
    private final String phone = "01012345678";
    private final String birth = "20000327";
    private final Long memberId = 3L;
    private final Long itemId = 14L;
    private final Long preferenceItemId = 2L;
    @Mock
    PreferenceRepository preferenceRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    ItemRepository itemRepository;
    @InjectMocks
    PreferenceService preferenceService;

    @Test
    @DisplayName("찜 상품 추가 테스트")
    void savePreferenceItem() {
        //given
        PreferenceItem preferenceItem = new PreferenceItem(preferenceItemId);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(new Member()));
        given(itemRepository.findById(itemId)).willReturn(Optional.of(new Item()));
        given(preferenceRepository.save(any(PreferenceItem.class))).willReturn(preferenceItem);

        //when
        Long preferenceItemId = preferenceService.savePreferenceItem(memberId, itemId);

        //then
        assertThat(preferenceItemId).isEqualTo(this.preferenceItemId);

        verify(memberRepository).findById(memberId);
        verify(itemRepository).findById(itemId);
        verify(preferenceRepository).save(any(PreferenceItem.class));
    }

    @Test
    @DisplayName("찜 목록 찾기 테스트")
    void findPreferenceByMemberId() {
        //given
        Member member = new Member(email, password, name, phone, birth, memberId);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        //when
        List<PreferenceDto> result = preferenceService.findPreferenceByMemberId(memberId);

        //then
        assertThat(result).isEmpty();

        verify(memberRepository).findById(memberId);
    }

    @Test
    @DisplayName("찜 목록 삭제 테스트")
    void deletePreference() {
        Member member = new Member(email, password, name, phone, birth, memberId);
        PreferenceItem preferenceItem = new PreferenceItem(member, new Item());
        //given
        given(preferenceRepository.findById(preferenceItemId)).willReturn(Optional.of(preferenceItem));
        doNothing().when(preferenceRepository).delete(any(PreferenceItem.class));

        //when
        preferenceService.deletePreference(memberId, preferenceItemId);

        assertThat(preferenceItem.getItem().getPreferenceCount()).isEqualTo(-1);

        verify(preferenceRepository).findById(preferenceItemId);
        verify(preferenceRepository).delete(any(PreferenceItem.class));
    }

    @Test
    @DisplayName("해당 아이템이 찜한 상품인지 확인 테스")
    void isPreference() {
        Member member = new Member(email, password, name, phone, birth, memberId);
        PreferenceItem preferenceItem = new PreferenceItem(member, new Item());
        preferenceItem.setId(preferenceItemId);
        //given
        given(preferenceRepository.findByMemberIdAndItemId(memberId, itemId)).willReturn(Optional.of(preferenceItem));
        given(preferenceRepository.findByMemberIdAndItemId(memberId + 1L, itemId + 1L)).willReturn(Optional.empty());

        //when
        IsPreference isPreferenceExist = preferenceService.isPreference(memberId, itemId);
        IsPreference isPreferenceNull = preferenceService.isPreference(memberId + 1L, itemId + 1L);

        assertThat(isPreferenceExist.getPreferenceId()).isEqualTo(preferenceItemId);
        assertThat(isPreferenceExist.isLogin()).isTrue();
        assertThat(isPreferenceExist.isPreference()).isTrue();

        assertThat(isPreferenceNull.getPreferenceId()).isNull();
        assertThat(isPreferenceNull.isLogin()).isTrue();
        assertThat(isPreferenceNull.isPreference()).isFalse();

        verify(preferenceRepository).findByMemberIdAndItemId(memberId, itemId);
        verify(preferenceRepository).findByMemberIdAndItemId(memberId + 1L, itemId + 1L);
    }
}