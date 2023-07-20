package konkuk.shop.domain.qna.api;

import konkuk.shop.dto.FindQnaDto;
import konkuk.shop.domain.qna.dto.RequestAddQnaForm;
import konkuk.shop.domain.qna.application.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {
    private final QnaService qnaService;

    @PostMapping
    public HashMap<String, Object> addQna(@AuthenticationPrincipal Long userId,
                                          @RequestBody RequestAddQnaForm form) {
        Long qnaId = qnaService.saveQna(userId, form);

        HashMap<String, Object> result = new HashMap<>();
        result.put("qnaId", qnaId);
        return result;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<List<FindQnaDto>> findQnaByItemId(@AuthenticationPrincipal Long userId,
                                                            @PathVariable Long itemId){
        List<FindQnaDto> result = qnaService.findQnaByItemId(userId, itemId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
