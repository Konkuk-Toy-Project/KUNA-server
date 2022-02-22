package konkuk.shop.service;

import konkuk.shop.dto.FindQnaDto;
import konkuk.shop.entity.AdminMember;
import konkuk.shop.entity.Item;
import konkuk.shop.entity.Member;
import konkuk.shop.entity.Qna;
import konkuk.shop.error.ApiException;
import konkuk.shop.error.ExceptionEnum;
import konkuk.shop.form.requestForm.qna.RequestAddQnaForm;
import konkuk.shop.form.responseForm.admin.ResponseQnaList;
import konkuk.shop.repository.AdminMemberRepository;
import konkuk.shop.repository.ItemRepository;
import konkuk.shop.repository.MemberRepository;
import konkuk.shop.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaService {
    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final AdminMemberRepository adminMemberRepository;


    public Long saveQna(Long userId, RequestAddQnaForm form) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_MEMBER));
        Item item = itemRepository.findById(form.getItemId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ITEM_BY_ID));

        log.info("qna 등록 요청. memberId={}, isSecret={}", userId, form.isSecret());
        Qna qna = new Qna(item, member, item.getAdminMember(), form.getQuestion(), form.isSecret(), form.getTitle());
        Qna saveQna = qnaRepository.save(qna);

        return saveQna.getId();
    }

    public List<FindQnaDto> findQnaByItemId(Long userId, Long itemId) {
        List<Qna> qnaList = qnaRepository.findAllByItemId(itemId);

        List<FindQnaDto> result = new ArrayList<>();

        for (Qna qna : qnaList) {
            if (qna.isSecret() && !qna.getMember().getId().equals(userId)) { // 남이 올린 비밀글
                result.add(FindQnaDto.builder()
                        .isSecret(qna.isSecret())
                        .memberName(qna.getMember().getName())
                        .isAnswered(qna.isAnswered())
                        .build());
            } else {
                result.add(FindQnaDto.builder()
                        .answer(qna.getAnswer())
                        .isAnswered(qna.isAnswered())
                        .isSecret(qna.isSecret())
                        .memberName(qna.getMember().getName())
                        .question(qna.getQuestion())
                        .registryDate(qna.getRegistryDate())
                        .title(qna.getTitle())
                        .build());
            }
        }
        log.info("qna 목록 요청. memberId={}, itemId={}", userId, itemId);
        return result;
    }

    public List<ResponseQnaList> findQnaByAdminMember(Long userId, boolean isAnswered) {
        AdminMember adminMember = adminMemberRepository.findByMemberId(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_ADMIN_MEMBER));

        log.info("자신의 상품 Qna 목록 요청. memberId={}, adminMemberId={}", userId, adminMember.getId());

        List<ResponseQnaList> result = qnaRepository.findAllByAdminMember(adminMember).stream()
                .filter(q -> q.isAnswered() == isAnswered)
                .map(q -> ResponseQnaList.builder()
                        .memberName(q.getMember().getName())
                        .isAnswered(isAnswered)
                        .isSecret(q.isSecret())
                        .qnaId(q.getId())
                        .itemName(q.getItem().getName())
                        .question(q.getQuestion())
                        .registryDate(q.getRegistryDate())
                        .itemId(q.getItem().getId())
                        .answer(checkNull(q.getAnswer()))
                        .title(q.getTitle())
                        .build())
                .collect(Collectors.toList());

        return result;
    }

    @Transactional
    public void saveAnswer(Long userId, Long qnaId, String answer) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FIND_QNA));

        if(qna.isAnswered()) throw new ApiException(ExceptionEnum.ALREADY_ANSWER_QNA);

        if (!qna.getAdminMember().getMember().getId().equals(userId))
            throw new ApiException(ExceptionEnum.NO_AUTHORITY_ANSWER_QNA);

        log.info("Qna에 답변 저장 요청. qnaId={}", qnaId);

        qna.registryAnswer(answer);
    }

    private String checkNull(String str) {
        return (str == null) ? "" : str;
    }
}

