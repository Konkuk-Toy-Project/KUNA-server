package konkuk.shop.service;

import konkuk.shop.dto.CartItemDto;
import konkuk.shop.entity.*;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final Option1Repository option1Repository;
    private final Option2Repository option2Repository;

    @Transactional
    public Long addItem(Long userId, Long itemId, Long option1Id, Long option2Id, int count) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));
        Option1 option1 = option1Repository.findById(option1Id)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_OPTION1_BY_ID));
        Option2 option2 = null;

        // 1. option2가 있어야 하는데 없는 경우
        if (option2Repository.existsByOption1(option1) && option2Id == null) {
            throw new ApiException(ExceptionEnum.NECESSARY_OPTION2);
        }

        // 2. option2가 없어야 하는데 있는 경우 또는 잘못된 id인 경우
        if (option2Id != null) {
            option2 = option2Repository.findById(option2Id)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_OPTION2_BY_ID));
            if (!option2.getOption1().getId().equals(option1.getId()))
                throw new ApiException(ExceptionEnum.NO_MATCH_OPTION2_WITH_OPTION1);
        }

        if (!option1.getItem().getId().equals(item.getId()))
            throw new ApiException(ExceptionEnum.NO_MATCH_OPTION1_WITH_ITEM);

        log.info("장바구니에 상품 담기 요청. userId={}, itemId={}, option1Id={}, option2Id={}",
                userId, itemId, option1Id, option2Id);

        CartItem cartItem = CartItem.builder()
                .item(item)
                .itemVersion(item.getVersion())
                .member(member)
                .option1(option1)
                .option2(option2)
                .count(count)
                .build();

        CartItem saveCartItem = cartRepository.save(cartItem);
        member.getCartItems().add(saveCartItem);
        return saveCartItem.getId();
    }

    public void deleteItemInCart(Long cartItemId) {
        cartRepository.delete(
                cartRepository.findById(cartItemId)
                        .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CART_ITEM))
        );
        log.info("장바구니에서 상품 삭제 요청. cartItemId={}", cartItemId);
    }

    public List<CartItemDto> findAllByUserId(Long userId) {
        List<CartItem> cartItems = cartRepository.findByMemberId(userId);

        List<CartItemDto> result = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            // 아이템 버전이 다르거나(수정), 삭제된 아이템은 조회시 장바구니에서 자동 삭제
            if (!itemRepository.existsById(item.getId())) cartRepository.delete(cartItem);
            else if (!cartItem.getItemVersion().equals(item.getVersion())) cartRepository.delete(cartItem);
            else {
                CartItemDto cartItemDto = CartItemDto.builder()
                        .thumbnailUrl(item.getThumbnail().getStore_name())
                        .option1(cartItem.getOption1().getName())
                        .count(cartItem.getCount())
                        .price(item.getPrice())
                        .sale(item.getSale())
                        .cartItemId(cartItem.getId())
                        .build();
                if (cartItem.getOption2() != null) cartItemDto.setOption2(cartItem.getOption2().getName());

                result.add(cartItemDto);
            }
        }
        log.info("장바구니 목록 요청 userId={}", userId);
        return result;
    }

    @Transactional
    public void changeCount(Long userId, Long cartItemId, Integer count) {
        CartItem cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CART_ITEM));
        if (!cartItem.getMember().getId().equals(userId))
            throw new ApiException(ExceptionEnum.NOT_AUTHORITY_CART_EDIT);
        log.info("장바구니 상품(cartItemId={}) 개수 {}->{}", cartItemId, cartItem.getCount(), count);

        cartItem.setCount(count);
    }
}
