package com.farmdora.farmdora.admin.repository;

import com.farmdora.farmdora.admin.dto.PopupSearchRequestDto;
import com.farmdora.farmdora.entity.Popup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPopupRepository {
    Page<Popup> searchPopups(PopupSearchRequestDto searchCondition, Pageable pageable);
}
