package konkuk.shop.service;

import konkuk.shop.dto.CartItemDto;
import konkuk.shop.entity.*;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public void addItem(Long userId, Long itemId, Long option1Id, Long option2Id, int count) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));
        Option1 option1 = option1Repository.findById(option1Id)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_OPTION1_BY_ID));
        Option2 option2 = option2Repository.findById(option2Id)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_OPTION2_BY_ID));
        Integer itemVersion = item.getVersion();

        CartItem cartItem = CartItem.builder()
                .item(item)
                .itemVersion(itemVersion)
                .member(member)
                .option1(option1)
                .option2(option2)
                .count(count)
                .build();

        CartItem saveCartItem = cartRepository.save(cartItem);
        member.getCartItems().add(saveCartItem);
        memberRepository.save(member);
    }

    public void deleteItem(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    public List<CartItemDto> findAllByUserId(Long userId) {
        List<CartItem> cartItems = cartRepository.findByMemberId(userId);

        List<CartItemDto> result = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            if (cartItem.getItemVersion() != item.getVersion()) cartRepository.delete(cartItem);

            CartItemDto cartItemDto = CartItemDto.builder()
                    .thumbnailUrl(item.getThumbnail().getStore_name())
                    .option1(cartItem.getOption1().getName())
                    .option2(cartItem.getOption2().getName())
                    .count(cartItem.getCount())
                    .price(item.getPrice())
                    .sale(item.getSale())
                    .cartItemId(cartItem.getId())
                    .build();

            result.add(cartItemDto);
        }

        return result;
    }

    public void changeCount(Long userId, Long cartItemId, Integer count) {
        CartItem cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_CART_ITEM));
        if(cartItem.getMember().getId() != userId) throw new ApiException(ExceptionEnum.NOT_AUTHORITY_CART_EDIT);

        cartItem.setCount(count);
        cartRepository.save(cartItem);
    }
}
