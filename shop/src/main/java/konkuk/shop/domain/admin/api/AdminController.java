package konkuk.shop.domain.admin.api;


import konkuk.shop.domain.admin.application.AdminManageItemService;
import konkuk.shop.domain.admin.dto.RequestAnswerQnaForm;
import konkuk.shop.domain.admin.dto.EditPriceAndSaleForm;
import konkuk.shop.domain.admin.dto.ResponseQnaList;
import konkuk.shop.domain.item.dto.ItemInfoDto;
import konkuk.shop.domain.qna.application.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    /**
     * 어드민 페이지 -> 스프링 시큐리티 권한 통해서 접근 가능?
     */
    private final QnaService qnaService;
    private final AdminManageItemService adminManageItemService;

    @GetMapping("/items")
    public ResponseEntity<List<ItemInfoDto>> myItemList(@AuthenticationPrincipal Long userId) {
        List<ItemInfoDto> result = adminManageItemService.findItemByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/qna/{isAnswered}")
    public ResponseEntity<List<ResponseQnaList>> findQna(@AuthenticationPrincipal Long userId,
                                                                @PathVariable Boolean isAnswered) {
        List<ResponseQnaList> result = qnaService.findQnaByAdminMember(userId, isAnswered);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/qna/{qnaId}")
    public ResponseEntity<?> saveAnswer(@AuthenticationPrincipal Long userId,
                                                                @PathVariable Long qnaId, @RequestBody RequestAnswerQnaForm form) {
        qnaService.saveAnswer(userId, qnaId, form.getAnswer());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/price/{itemId}")
    public void editPriceByItemId(@AuthenticationPrincipal Long userId, @PathVariable Long itemId,
                                  @RequestBody EditPriceAndSaleForm form) {
        adminManageItemService.editPriceByItemId(userId, itemId, form.getPrice(), form.getSale());
    }

}
