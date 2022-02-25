package konkuk.shop.service;

import konkuk.shop.entity.Item;
import konkuk.shop.entity.Member;
import konkuk.shop.entity.PreferenceItem;
import konkuk.shop.repository.ItemRepository;
import konkuk.shop.repository.MemberRepository;
import konkuk.shop.repository.PreferenceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PreferenceServiceTest {
    @InjectMocks
    private PreferenceService preferenceService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private PreferenceRepository preferenceRepository;

    @Test
    @DisplayName("테스트")
    @Transactional
    void savePreferenceItem(){
        //given
        doReturn(Optional.of(new Member())).when(memberRepository).findById(any(Long.class));
        doReturn(Optional.of(Item.builder().preferenceCount(0).build())).when(itemRepository).findById(any(Long.class));
        doReturn(new PreferenceItem()).when(preferenceRepository).save(any(PreferenceItem.class));

        //when
        Long preferenceId = preferenceService.savePreferenceItem(0L, 0L);

        //then
        assertThat(preferenceId).isNull();
    }
}