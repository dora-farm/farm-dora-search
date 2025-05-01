package com.farmdora.farmdora.admin.service;

import com.farmdora.farmdora.admin.dto.PopupSearchRequestDto;
import com.farmdora.farmdora.admin.dto.PopupSearchResponseDto;
import com.farmdora.farmdora.admin.repository.PopupRepository;
import com.farmdora.farmdora.common.NcpImageProperties;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.entity.Popup;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PopupService {
    private final PopupRepository popupRepository;
    private final NcpImageProperties ncpImageProperties;

    @Transactional(readOnly = true)
    public PageResponseDto<PopupSearchResponseDto> searchPopups(PopupSearchRequestDto searchCondition, Pageable pageable) {
        Page<Popup> popups = popupRepository.searchPopups(searchCondition, pageable);
        List<PopupSearchResponseDto> popupSearchResponses = popups.getContent().stream()
                .map(p -> PopupSearchResponseDto.fromEntity(p,
                        ncpImageProperties.getPath(),
                        ncpImageProperties.getType()))
                .toList();
        return new PageResponseDto<>(popupSearchResponses, popups);
    }
}
