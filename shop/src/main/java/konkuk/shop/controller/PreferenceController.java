package konkuk.shop.controller;


import konkuk.shop.dto.PreferenceDto;
import konkuk.shop.entity.PreferenceItem;
import konkuk.shop.form.responseForm.member.ResponseSignupForm;
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

    @DeleteMapping
    public ResponseEntity<?> deletePreferenceItem(@AuthenticationPrincipal Long userId,
                                                                    @RequestBody HashMap<String, String> map) {
        Long preferenceId = Long.parseLong(map.get("preferenceId"));
        preferenceService.deletePreference(userId, preferenceId);

        return ResponseEntity.ok().build();
    }


}
