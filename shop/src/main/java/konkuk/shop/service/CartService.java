package konkuk.shop.service;

import konkuk.shop.entity.*;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
