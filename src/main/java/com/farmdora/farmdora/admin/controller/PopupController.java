package com.farmdora.farmdora.admin.controller;

import static com.farmdora.farmdora.common.response.SuccessMessage.GET_POPUPS_SUCCESS;
import static com.farmdora.farmdora.common.response.SuccessMessage.GET_POPUP_TYPES_SUCCESS;

import com.farmdora.farmdora.admin.dto.PopupSearchRequestDto;
import com.farmdora.farmdora.admin.dto.PopupSearchResponseDto;
import com.farmdora.farmdora.admin.dto.PopupTypeDto;
import com.farmdora.farmdora.admin.service.PopupService;
import com.farmdora.farmdora.common.response.HttpResponse;
import com.farmdora.farmdora.common.response.PageResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.prefix}/admin")
@RequiredArgsConstructor
public class PopupController {
    private final PopupService popupService;

    @GetMapping("/popup")
    public ResponseEntity<?> searchPopup(PopupSearchRequestDto searchCondition,
                                         @PageableDefault Pageable pageable) {
        PageResponseDto<PopupSearchResponseDto> popups = popupService.searchPopups(searchCondition, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, GET_POPUPS_SUCCESS.getMessage(), popups));
    }

    @GetMapping("/popup/type")
    public ResponseEntity<?> getPopupTypes() {
        List<PopupTypeDto> popupTypes = popupService.getPopupTypes();
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, GET_POPUP_TYPES_SUCCESS.getMessage(), popupTypes));
    }
}
