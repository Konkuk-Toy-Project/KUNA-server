package konkuk.shop.domain.qna.application;

import konkuk.shop.dto.FindQnaDto;
import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.qna.entity.Qna;
import konkuk.shop.global.exception.ApplicationException;
import konkuk.shop.global.exception.ErrorCode;
import konkuk.shop.domain.qna.dto.RequestAddQnaForm;
import konkuk.shop.domain.admin.dto.ResponseQnaList;
import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.item.repository.ItemRepository;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.domain.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_MEMBER));
        Item item = itemRepository.findById(form.getItemId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_ITEM_BY_ID));

        log.info("qna 등록 요청. memberId={}, isSecret={}", userId, form.isSecret());
        Qna qna = new Qna(item, member, item.getAdminMember(), form.getQuestion(), form.isSecret(), form.getTitle());
        Qna saveQna = qnaRepository.save(qna);

        return saveQna.getId();
    }

    public List<FindQnaDto> findQnaByItemId(Long userId, Long itemId) {
        log.info("qna 목록 요청. memberId={}, itemId={}", userId, itemId);

        return qnaRepository.findAllByItemId(itemId).stream()
                .map(e -> {
                    if (isSecretByOthers(e, userId)) return buildFindQnaDtoSecret(e);
                    else return buildFindQnaDtoNotSecret(e);
                })
                .collect(Collectors.toList());
    }

    private boolean isSecretByOthers(Qna qna, Long userId) {
        return qna.isSecret() && !qna.getMember().getId().equals(userId);
    }

    private FindQnaDto buildFindQnaDtoSecret(Qna qna) {
        return FindQnaDto.builder()
                .isSecret(qna.isSecret())
                .memberName(qna.getMember().getName())
                .isAnswered(qna.isAnswered())
                .build();
    }

    private FindQnaDto buildFindQnaDtoNotSecret(Qna qna) {
        return FindQnaDto.builder()
                .answer(qna.getAnswer())
                .isAnswered(qna.isAnswered())
                .isSecret(qna.isSecret())
                .memberName(qna.getMember().getName())
                .question(qna.getQuestion())
                .registryDate(qna.getRegistryDate())
                .title(qna.getTitle())
                .build();
    }


    public List<ResponseQnaList> findQnaByAdminMember(Long userId, boolean isAnswered) {
        AdminMember adminMember = adminMemberRepository.findByMemberId(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_ADMIN_MEMBER));

        log.info("자신의 상품 Qna 목록 요청. memberId={}, adminMemberId={}", userId, adminMember.getId());

        return qnaRepository.findAllByAdminMember(adminMember).stream()
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
    }

    @Transactional
    public void saveAnswer(Long userId, Long qnaId, String answer) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_FIND_QNA));

        if (qna.isAnswered()) throw new ApplicationException(ErrorCode.ALREADY_ANSWER_QNA);

        if (!qna.getAdminMember().getMember().getId().equals(userId))
            throw new ApplicationException(ErrorCode.NO_AUTHORITY_ANSWER_QNA);

        log.info("Qna에 답변 저장 요청. qnaId={}", qnaId);

        qna.registryAnswer(answer);
    }

    private String checkNull(String str) {
        return (str == null) ? "" : str;
    }
}

