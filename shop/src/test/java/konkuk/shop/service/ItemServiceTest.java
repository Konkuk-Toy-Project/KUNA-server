package konkuk.shop.service;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.category.entity.Category;
import konkuk.shop.domain.category.repository.CategoryRepository;
import konkuk.shop.domain.image.entity.Thumbnail;
import konkuk.shop.domain.item.application.ItemService;
import konkuk.shop.domain.item.dto.RequestAddItemDto;
import konkuk.shop.domain.item.dto.ResponseItemDetail;
import konkuk.shop.domain.item.dto.ResponseMyItem;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class ItemServiceTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    AdminMemberRepository adminMemberRepository;
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    ItemService itemService;


    private final String itemName = "TestItem";
    private final Integer price = 50000;
    private final Integer sale = 20;
    private final Long categoryId = 1L;
    private final String categoryName = "TestCategory";
    private final Long itemId = 14L;
    private final Long memberId = 3L;
    private final Long AdminMemberId = 4L;
    private Item item;
    private AdminMember adminMember;

    @BeforeEach
    void dataSetting() {
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", memberId);
        adminMember = new AdminMember(AdminMemberId, member);

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
    @DisplayName("상품 추가 테스트")
    void addItem() {
        //given
        RequestAddItemDto form = new RequestAddItemDto(itemName, price, sale, categoryId, null, new ArrayList<>(), new ArrayList<>());

        given(adminMemberRepository.findByMemberId(1L)).willReturn(Optional.of(new AdminMember()));
        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(new Category()));
        given(itemRepository.save(any(Item.class))).willReturn(item);

        //when
        Long saveItemId = itemService.addItem(1L, form);

        //then
        verify(adminMemberRepository).findByMemberId(1L);
        verify(categoryRepository).findById(categoryId);
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    @DisplayName("카테고리로 상품 찾기 테스트")
    void findItemListByCategory() {
        //given
        List<Item> items = new ArrayList<>();
        items.add(item);
        given(itemRepository.findByCategoryId(categoryId)).willReturn(items);

        //when
        List<ResponseMyItem> result = itemService.findItemListByCategory(categoryId);
        ResponseMyItem response = result.get(0);

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
    @DisplayName("상품의 option 추가하기 테스트")
    void saveOption() {
        // given
        given(itemRepository.findById(itemId)).willReturn(Optional.of(item));

        //when
        itemService.saveOption(memberId, new ArrayList<>(), itemId);

        //then
        verify(itemRepository).findById(itemId);
    }

    @Test
    @DisplayName("상품 찾기 테스트")
    void findItemById() {
        // given
        given(itemRepository.findById(itemId)).willReturn(Optional.of(item));

        //when
        ResponseItemDetail result = itemService.findItemById(itemId);

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
        List<Item> items = new ArrayList<>();
        items.add(item);
        given(itemRepository.findAll()).willReturn(items);

        //when
        List<ResponseMyItem> result = itemService.findItemBySearchWord("test");
        ResponseMyItem response = result.get(0);

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
    @DisplayName("해당 관리자가 등록한 상품 찾기 테스트")
    void findItemByUserId() {
        //given
        List<Item> items = new ArrayList<>();
        items.add(item);
        given(adminMemberRepository.findByMemberId(memberId)).willReturn(Optional.of(new AdminMember()));
        given(itemRepository.findByAdminMember(any(AdminMember.class))).willReturn(items);

        //when
        List<ResponseMyItem> result = itemService.findItemByUserId(memberId);
        ResponseMyItem response = result.get(0);

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
    @DisplayName("모든 상품 조회 테스트")
    void findAllItem() {
        //given
        List<Item> items = new ArrayList<>();
        items.add(item);
        given(itemRepository.findAll()).willReturn(items);

        //when
        List<ResponseMyItem> result = itemService.findAllItem();
        ResponseMyItem response = result.get(0);

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
    @DisplayName("상품 가격 수정 테스트")
    void editPriceByItemId() {
        //given
        given(adminMemberRepository.findByMemberId(memberId)).willReturn(Optional.of(adminMember));
        given(itemRepository.findById(itemId)).willReturn(Optional.of(item));

        //when
        itemService.editPriceByItemId(memberId, itemId, 5000, 10);

        //then
        assertThat(item.getPrice()).isEqualTo(5000);
        assertThat(item.getSale()).isEqualTo(10);

        verify(itemRepository).findById(itemId);
        verify(adminMemberRepository).findByMemberId(memberId);
    }
}