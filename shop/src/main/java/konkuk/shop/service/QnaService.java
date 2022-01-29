package konkuk.shop.service;


import konkuk.shop.dto.FindQnaDto;
import konkuk.shop.entity.Item;
import konkuk.shop.entity.Member;
import konkuk.shop.entity.Qna;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.form.requestForm.qna.RequestAddQnaForm;
import konkuk.shop.repository.ItemRepository;
import konkuk.shop.repository.MemberRepository;
import konkuk.shop.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaService {
    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;


    public Long saveQna(Long userId, RequestAddQnaForm form) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        Item item = itemRepository.findById(form.getItemId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));

        log.info("qna request. isSecret={}", form.isSecret());
        Qna qna = new Qna(item, member, item.getAdminMember(), form.getQuestion(), form.isSecret());
        Qna saveQna = qnaRepository.save(qna);

        return saveQna.getId();
    }

    public List<FindQnaDto> findQnaByItemId(Long userId, Long itemId) {
        List<Qna> qnaList = qnaRepository.findAllByItemId(itemId);

        List<FindQnaDto> result = new ArrayList<>();

        for (Qna qna : qnaList) {
            log.info("qnaUserId={}   loginId={}", qna.getMember().getId(), userId);
            if (qna.isSecret() && !qna.getMember().getId().equals(userId)) { // 남이 올린 비밀글
                result.add(FindQnaDto.builder()
                        .isSecret(qna.isSecret())
                        .memberName(qna.getMember().getName())
                        .build());
            } else {
                result.add(FindQnaDto.builder()
                        .answer(qna.getAnswer())
                        .isAnswered(qna.isAnswered())
                        .isSecret(qna.isSecret())
                        .memberName(qna.getMember().getName())
                        .question(qna.getQuestion())
                        .registryDate(qna.getRegistryDate())
                        .build());
            }
        }
        return result;
    }
}

