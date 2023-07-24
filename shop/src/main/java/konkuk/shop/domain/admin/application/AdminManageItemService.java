package konkuk.shop.domain.admin.application;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.admin.exception.AdminNotFoundException;
import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.item.dto.ItemInfoDto;
import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.item.repository.ItemRepository;
import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminManageItemService {
    private final ItemRepository itemRepository;
    private final AdminMemberRepository adminMemberRepository;

    @Transactional(readOnly = true)
    public List<ItemInfoDto> findItemByUserId(Long userId) {
        AdminMember adminMember = adminMemberRepository.findByMemberId(userId)
                .orElseThrow(AdminNotFoundException::new);

        return itemRepository.findByAdminMember(adminMember).stream()
                .map(e -> ItemInfoDto.builder()
                        .itemState(e.getItemState().toString())
                        .name(e.getName())
                        .price(e.getPrice())
                        .sale(e.getSale())
                        .thumbnailUrl(e.getThumbnail().getStore_name())
                        .preference(e.getPreferenceCount())
                        .itemId(e.getId())
                        .categoryId(e.getCategory().getId())
                        .categoryName(e.getCategory().getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void editPriceByItemId(Long userId, Long itemId, Integer price, Integer sale) {
        AdminMember adminMember = adminMemberRepository.findByMemberId(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_ADMIN_MEMBER));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_ITEM));

        if (!adminMember.getId().equals(item.getAdminMember().getId()))
            throw new ApplicationException(ErrorCode.NO_AUTHORITY_ACCESS_ITEM);

        item.changePriceAndSale(price, sale);
    }
}
