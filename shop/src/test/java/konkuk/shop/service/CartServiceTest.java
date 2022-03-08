package konkuk.shop.service;

import konkuk.shop.dto.CartItemDto;
import konkuk.shop.entity.CartItem;
import konkuk.shop.entity.Item;
import konkuk.shop.entity.Member;
import konkuk.shop.entity.Option1;
import konkuk.shop.repository.*;
import konkuk.shop.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class CartServiceTest {
    @Mock
    MemberRepository memberRepository;
    @Mock
    CartRepository cartRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    Option1Repository option1Repository;
    @Mock
    Option2Repository option2Repository;
    @InjectMocks
    CartService cartService;
    private final Long memberId = 3L;
    private final Long itemId = 14L;
    private final Long option1Id = 8L;
    private final Long cartItemId = 9L;

    @Test
    @DisplayName("장바구니 상품 추가 테스트")
    void addItem() {
        //given
        Item item = new Item(1L);
        Option1 option1 = new Option1();
        option1.setItem(item);
        CartItem cartItem = new CartItem(cartItemId);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(new Member()));
        given(itemRepository.findById(itemId)).willReturn(Optional.of(item));
        given(option1Repository.findById(option1Id)).willReturn(Optional.of(option1));
        given(option2Repository.existsByOption1(any(Option1.class))).willReturn(false);
        given(cartRepository.save(any(CartItem.class))).willReturn(cartItem);


        //when
        Long cartItemId = cartService.addItem(memberId, itemId, option1Id, null, 3);

        //then
        assertThat(cartItemId).isEqualTo(cartItemId);
        verify(memberRepository).findById(memberId);
        verify(itemRepository).findById(itemId);
        verify(option1Repository).findById(option1Id);
        verify(option2Repository).existsByOption1(any(Option1.class));
        verify(cartRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("장바구니 상품 삭제 테스트")
    void deleteItemInCart() {
        //given
        CartItem cartItem = new CartItem(cartItemId, new Member(memberId));

        willDoNothing().given(cartRepository).delete(any(CartItem.class));
        given(cartRepository.findById(cartItemId)).willReturn(Optional.of(cartItem));

        //when
        cartService.deleteItemInCart(memberId, cartItemId);

        //then
        verify(cartRepository).findById(cartItemId);
        verify(cartRepository).delete(any(CartItem.class));
    }

    @Test
    @DisplayName("장바구니 상품 모두 찾기 테스트")
    void findAllByUserId() {
        //given
        given(cartRepository.findByMemberId(memberId)).willReturn(new ArrayList<>());

        //when
        List<CartItemDto> result = cartService.findAllByUserId(memberId);

        //then
        assertThat(result).isEmpty();
        verify(cartRepository).findByMemberId(memberId);
    }

    @Test
    @DisplayName("장바구니 상품 개수 바꾸기 테스트")
    void changeCount() {
        //given
        CartItem cartItem = new CartItem(cartItemId, new Member(memberId));
        given(cartRepository.findById(cartItemId)).willReturn(Optional.of(cartItem));

        //when
        cartService.changeCount(memberId, cartItemId, 1);

        //then
        verify(cartRepository).findById(cartItemId);
    }
}