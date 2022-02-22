package konkuk.shop.controller;


import konkuk.shop.dto.IsPreference;
import konkuk.shop.dto.PreferenceDto;
import konkuk.shop.entity.PreferenceItem;
import konkuk.shop.service.PreferenceService;
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
@RequestMapping("/preference")
public class PreferenceController {
    private final PreferenceService preferenceService;

    @PostMapping
    public HashMap<String, Object> addPreferenceItem(@AuthenticationPrincipal Long userId,
                                                     @RequestBody HashMap<String, String> map) {
        Long itemId = Long.parseLong(map.get("itemId"));
        Long preferenceId = preferenceService.savePreferenceItem(userId, itemId);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("preferenceId", preferenceId);

        return result;
    }

    @GetMapping
    public ResponseEntity<List<PreferenceDto>> getPreferenceItem(@AuthenticationPrincipal Long userId) {

        List<PreferenceDto> preferenceDtoList = preferenceService.findPreferenceByMemberId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(preferenceDtoList);
    }

    @DeleteMapping("/{preferenceId}")
    public ResponseEntity<?> deletePreferenceItem(@AuthenticationPrincipal Long userId,
                                                  @PathVariable Long preferenceId) {
        preferenceService.deletePreference(userId, preferenceId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/isPreference/{itemId}")
    public ResponseEntity<IsPreference> isPreferenceItem(@AuthenticationPrincipal Long userId, @PathVariable Long itemId) {
        log.info("현재 상품이 찜한 상품인지 확인하는 요청. itemId={}", itemId);


        IsPreference result;

        if(userId==null) result = new IsPreference(false, false, null);
        else result = preferenceService.isPreference(userId, itemId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
