package konkuk.shop.service;

import konkuk.shop.domain.admin.entity.AdminMember;
import konkuk.shop.domain.item.entity.Item;
import konkuk.shop.domain.member.entity.Member;
import konkuk.shop.domain.qna.application.QnaService;
import konkuk.shop.domain.qna.entity.Qna;
import konkuk.shop.dto.FindQnaDto;
import konkuk.shop.domain.qna.dto.RequestAddQnaForm;
import konkuk.shop.domain.admin.dto.ResponseQnaList;
import konkuk.shop.domain.admin.repository.AdminMemberRepository;
import konkuk.shop.domain.item.repository.ItemRepository;
import konkuk.shop.domain.member.repository.MemberRepository;
import konkuk.shop.domain.qna.repository.QnaRepository;
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
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class QnaServiceTest {
    private final Long memberId = 3L;
    private final Long itemId = 14L;
    private final Long option1Id = 8L;
    private final Long cartItemId = 9L;
    private final Long qnaId = 56L;
    private final String question = "질문본문";
    private final String title = "질문제목";
    private final String answer = "질문답변";
    @Mock
    QnaRepository qnaRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    AdminMemberRepository adminMemberRepository;
    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    QnaService qnaService;

    @Test
    @DisplayName("qna 등록 테스트")
    void saveQna() {
        //given
        Item item = new Item(itemId);

        RequestAddQnaForm form = new RequestAddQnaForm(itemId, false, question, title);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(new Member()));
        given(itemRepository.findById(itemId)).willReturn(Optional.of(item));
        given(qnaRepository.save(any(Qna.class))).willReturn(new Qna());


        //when
        Long cartItemId = qnaService.saveQna(memberId, form);

        //then
        assertThat(cartItemId).isNull();
        verify(memberRepository).findById(memberId);
        verify(itemRepository).findById(itemId);
        verify(qnaRepository).save(any(Qna.class));
    }

    @Test
    @DisplayName("상품 id로 qna 찾기 테스트")
    void findQnaByItemId() {
        //given
        given(qnaRepository.findAllByItemId(itemId)).willReturn(new ArrayList<>());


        //when
        List<FindQnaDto> result = qnaService.findQnaByItemId(memberId, itemId);

        //then
        assertThat(result).hasSize(0);
        verify(qnaRepository).findAllByItemId(itemId);
    }

    @Test
    @DisplayName("관리자로 qna 찾기 테스트")
    void findQnaByAdminMember() {
        //given
        given(adminMemberRepository.findByMemberId(memberId)).willReturn(Optional.of(new AdminMember()));
        given(qnaRepository.findAllByAdminMember(any(AdminMember.class))).willReturn(new ArrayList<>());


        //when
        List<ResponseQnaList> result = qnaService.findQnaByAdminMember(memberId, false);

        //then
        assertThat(result).hasSize(0);
        verify(adminMemberRepository).findByMemberId(memberId);
        verify(qnaRepository).findAllByAdminMember(any(AdminMember.class));
    }

    @Test
    @DisplayName("답변하기 테스트")
    void saveAnswer() {
        Qna qna = new Qna(new Item(), new Member(), new AdminMember(new Member(memberId)),
                question, false, title);


        //given
        given(qnaRepository.findById(qnaId)).willReturn(Optional.of(qna));


        //when
        qnaService.saveAnswer(memberId, qnaId, answer);

        //then
        assertThat(qna.getAnswer()).isEqualTo(answer);
        verify(qnaRepository).findById(qnaId);
    }
}