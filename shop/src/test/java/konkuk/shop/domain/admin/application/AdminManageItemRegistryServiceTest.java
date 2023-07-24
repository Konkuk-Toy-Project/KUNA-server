package konkuk.shop.domain.admin.application;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.category.entity.Category;
import konkuk.shop.domain.image.entity.Thumbnail;
import konkuk.shop.domain.item.dto.ItemInfoDto;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminManageItemRegistryServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private AdminMemberRepository adminMemberRepository;

    @InjectMocks
    private AdminManageItemService adminManageItemService;

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
    @DisplayName("해당 관리자가 등록한 상품 찾기 테스트")
    void findItemByUserId() {
        //given
        List<Item> items = List.of(item);
        given(adminMemberRepository.findByMemberId(memberId))
                .willReturn(Optional.of(new AdminMember()));
        given(itemRepository.findByAdminMember(any(AdminMember.class)))
                .willReturn(items);

        //when
        List<ItemInfoDto> result = adminManageItemService.findItemByUserId(memberId);
        ItemInfoDto response = result.get(0);

        //then
        assertThat(response.getItemState()).isEqualTo(ItemState.NORMALITY.toString());
        assertThat(response.getName()).isEqualTo(itemName);
        assertThat(response.getPrice()).isEqualTo(price);
        assertThat(response.getSale()).isEqualTo(sale);
        assertThat(response.getPreference()).isEqualTo(0);
        assertThat(response.getItemId()).isEqualTo(itemId);
        assertThat(response.getCategoryName()).isEqualTo(categoryName);
        verify(itemRepository).findByAdminMember(any(AdminMember.class));
        verify(adminMemberRepository).findByMemberId(memberId);
    }

    @Test
    @DisplayName("상품 가격 수정 테스트")
    void editPriceByItemId() {
        //given
        given(adminMemberRepository.findByMemberId(memberId))
                .willReturn(Optional.of(adminMember));
        given(itemRepository.findById(itemId))
                .willReturn(Optional.of(item));

        //when
        adminManageItemService.editPriceByItemId(memberId, itemId, 5000, 10);

        //then
        assertThat(item.getPrice()).isEqualTo(5000);
        assertThat(item.getSale()).isEqualTo(10);
        verify(itemRepository).findById(itemId);
        verify(adminMemberRepository).findByMemberId(memberId);
    }
}