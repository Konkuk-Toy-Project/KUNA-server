package konkuk.shop.domain.item.application;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.category.entity.Category;
import konkuk.shop.domain.image.entity.Thumbnail;
import konkuk.shop.domain.item.dto.ItemDetailDto;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ItemFindServiceTest {

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ItemFindService itemFindService;

    private final String itemName = "TestItem";
    private final Integer price = 50000;
    private final Integer sale = 20;
    private final Long categoryId = 1L;
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
                .version(1)
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
    @DisplayName("카테고리로 상품 찾기 테스트")
    void findItemListByCategory() {
        //given
        List<Item> items = List.of(item);
        given(itemRepository.findByCategoryId(categoryId))
                .willReturn(items);

        //when
        List<ItemInfoDto> result = itemFindService.findItemListByCategory(categoryId);
        ItemInfoDto response = result.get(0);

        //then
        assertThat(response.getItemState()).isEqualTo(ItemState.NORMALITY.toString());
        assertThat(response.getName()).isEqualTo(itemName);
        assertThat(response.getPrice()).isEqualTo(price);
        assertThat(response.getSale()).isEqualTo(sale);
        assertThat(response.getPreference()).isEqualTo(0);
        assertThat(response.getItemId()).isEqualTo(itemId);
        assertThat(response.getCategoryName()).isEqualTo(categoryName);
        verify(itemRepository).findByCategoryId(categoryId);
    }

    @Test
    @DisplayName("상품 찾기 테스트")
    void findItemById() {
        // given
        given(itemRepository.findById(itemId))
                .willReturn(Optional.of(item));

        //when
        ItemDetailDto result = itemFindService.findItemById(itemId);

        //then
        assertThat(result.getItemState()).isEqualTo(ItemState.NORMALITY.toString());
        assertThat(result.getName()).isEqualTo(itemName);
        assertThat(result.getPrice()).isEqualTo(price);
        assertThat(result.getSale()).isEqualTo(sale);
        assertThat(result.getPreference()).isEqualTo(0);
        assertThat(result.getItemId()).isEqualTo(itemId);
        assertThat(result.getCategoryName()).isEqualTo(categoryName);
        verify(itemRepository).findById(itemId);
    }

    @Test
    @DisplayName("특정 검색어로 상품 찾기 테스트")
    void findItemBySearchWord() {
        //given
        List<Item> items = List.of(item);
        given(itemRepository.findAll())
                .willReturn(items);

        //when
        List<ItemInfoDto> result = itemFindService.findItemBySearchWord("test");
        ItemInfoDto response = result.get(0);

        //then
        assertThat(response.getItemState()).isEqualTo(ItemState.NORMALITY.toString());
        assertThat(response.getName()).isEqualTo(itemName);
        assertThat(response.getPrice()).isEqualTo(price);
        assertThat(response.getSale()).isEqualTo(sale);
        assertThat(response.getPreference()).isEqualTo(0);
        assertThat(response.getItemId()).isEqualTo(itemId);
        assertThat(response.getCategoryName()).isEqualTo(categoryName);
        verify(itemRepository).findAll();
    }


    @Test
    @DisplayName("모든 상품 조회 테스트")
    void findAllItem() {
        //given
        List<Item> items = List.of(item);
        given(itemRepository.findAll())
                .willReturn(items);

        //when
        List<ItemInfoDto> result = itemFindService.findAllItem();
        ItemInfoDto response = result.get(0);

        //then
        assertThat(response.getItemState()).isEqualTo(ItemState.NORMALITY.toString());
        assertThat(response.getName()).isEqualTo(itemName);
        assertThat(response.getPrice()).isEqualTo(price);
        assertThat(response.getSale()).isEqualTo(sale);
        assertThat(response.getPreference()).isEqualTo(0);
        assertThat(response.getItemId()).isEqualTo(itemId);
        assertThat(response.getCategoryName()).isEqualTo(categoryName);
        verify(itemRepository).findAll();
    }
}