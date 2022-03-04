package konkuk.shop.service;

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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
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
    TokenProvider tokenProvider;
    @Mock
    Option1Repository option1Repository;
    @Mock
    Option2Repository option2Repository;
    @InjectMocks
    CartService cartService;
    private final String name = "testMember";
    private final String email = "asdf@asdf.com";
    private final String password = "asdfasdf@1";
    private final String phone = "01012345678";
    private final String birth = "20000327";
    private final String role = "user";
    private final Long memberId = 3L;
    private final String token = "JWTToken";
    private final Long itemId = 14L;

    @Test
    @DisplayName("장바구니 상품 추가 테스트")
    void addItem() {
        //given
        given(memberRepository.findById(memberId)).willReturn(Optional.of(new Member()));
        given(itemRepository.findById(itemId)).willReturn(Optional.of(new Item()));
        given(option1Repository.findById(1L)).willReturn(Optional.of(new Option1()));

        //when
        boolean isDuplicateEmailTrue = memberService.isDuplicateEmail(email);
        boolean isDuplicateEmailFalse = memberService.isDuplicateEmail(email + "fake");

        //then
        assertThat(isDuplicateEmailTrue).isTrue();
        assertThat(isDuplicateEmailFalse).isFalse();
        verify(memberRepository).existsByEmail(email);
        verify(memberRepository).existsByEmail(email + "fake");
    }

    @Test
    @DisplayName("장바구니 상품 삭제 테스트")
    void deleteItemInCart() {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("장바구니 상품 검색 테스트")
    void findAllByUserId() {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("장바구니 상품 개수 바꾸기 테스트")
    void changeCount() {
        // given

        // when

        // then

    }
}