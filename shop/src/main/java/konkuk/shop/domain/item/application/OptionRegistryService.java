package konkuk.shop.domain.item.application;

import konkuk.shop.domain.item.dto.OptionOneForm;
import konkuk.shop.domain.item.dto.OptionTwoForm;
import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.item.entity.Option1;
import konkuk.shop.domain.item.entity.Option2;
import konkuk.shop.domain.item.exception.ItemNotFoundException;
import konkuk.shop.domain.item.exception.NoAuthorityAccessItem;
import konkuk.shop.domain.item.repository.ItemRepository;
import konkuk.shop.domain.item.repository.Option1Repository;
import konkuk.shop.domain.item.repository.Option2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionRegistryService {
    private final ItemRepository itemRepository;
    private final Option1Repository option1Repository;
    private final Option2Repository option2Repository;

    @Transactional
    public void saveOption(Long userId, List<OptionOneForm> option1s, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFoundException::new);
        validAuthorityToAccessItem(userId, item);
        saveOptionToItem(option1s, item);
    }

    private void saveOptionToItem(List<OptionOneForm> option1s, Item item) {
        for (OptionOneForm optionOneForm : option1s) {
            Option1 saveOption1 = getOption1(optionOneForm);
            option1Repository.save(saveOption1);
            saveOption1.setItem(item);
            item.getOption1s().add(saveOption1);
        }
    }

    private Option1 getOption1(OptionOneForm option1) {
        List<OptionTwoForm> option2s = option1.getOption2s();
        Option1 saveOption1 = new Option1(option1.getName(), option1.getStock());
        addOption2ToOption1(saveOption1, option2s);
        return saveOption1;
    }

    private void addOption2ToOption1(Option1 saveOption1, List<OptionTwoForm> option2s) {
        for (OptionTwoForm option2 : option2s) {
            Option2 saveOption2 = new Option2(option2.getStock(), option2.getName(), saveOption1);
            option2Repository.save(saveOption2);
            saveOption1.getOption2s().add(saveOption2);
        }
    }

    private void validAuthorityToAccessItem(Long userId, Item item) {
        if (!item.getAdminMember().getMember().getId().equals(userId)) {
            throw new NoAuthorityAccessItem();
        }
    }
}
