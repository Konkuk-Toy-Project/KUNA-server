package konkuk.shop.domain.item.application;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.category.entity.Category;
import konkuk.shop.domain.image.entity.Thumbnail;
import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.item.entity.ItemState;
import konkuk.shop.domain.item.repository.ItemRepository;
import konkuk.shop.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OptionRegistryServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private OptionRegistryService optionRegistryService;

    private final String itemName = "TestItem";
    private final Integer price = 50000;
    private final Integer sale = 20;
    private final String categoryName = "TestCategory";
    private final Long itemId = 14L;
    private final Long memberId = 3L;
    private final Long adminMemberId = 4L;
    private Item item;
    private AdminMember adminMember;

    @BeforeEach
    void dataSetting() {
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", memberId);

        adminMember = new AdminMember(member);
        ReflectionTestUtils.setField(adminMember, "id", adminMemberId);

        item = Item.builder()
                .itemState(ItemState.NORMALITY)
                .adminMember(new AdminMember())
                .name(itemName)
                .preferenceCount(0)
                .registryDate(LocalDateTime.now())
                .version(1) // 첫 번째 버전 : 1
                .sale(sale)
                .price(price)
                .category(new Category(categoryName))
                .option1s(new ArrayList<>())
                .itemImages(new ArrayList<>())
                .detailImages(new ArrayList<>())
                .thumbnail(new Thumbnail())
                .id(itemId)
                .adminMember(adminMember)
                .build();
    }

    @Test
    @DisplayName("상품의 option 추가하기 테스트")
    void saveOption() {
        // given
        given(itemRepository.findById(itemId))
                .willReturn(Optional.of(item));

        //when
        optionRegistryService.saveOption(memberId, new ArrayList<>(), itemId);

        //then
        verify(itemRepository).findById(itemId);
    }
}