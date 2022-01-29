package konkuk.shop.controller;


import konkuk.shop.form.responseForm.item.ResponseItemList;
import konkuk.shop.service.ItemService;
import konkuk.shop.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final ItemService itemService;

    @GetMapping("/items")
    public ResponseEntity<List<ResponseItemList>> myItemList(@AuthenticationPrincipal Long userId) {
        List<ResponseItemList> result = itemService.findItemByAdminMember(userId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
