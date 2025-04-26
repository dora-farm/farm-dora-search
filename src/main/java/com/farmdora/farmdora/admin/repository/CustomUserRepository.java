package com.farmdora.farmdora.admin.repository;

import com.farmdora.farmdora.admin.dto.UserSearchRequestDto;
import com.farmdora.farmdora.admin.dto.UserSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserRepository {
    Page<UserSearchResponseDto> searchUsers(UserSearchRequestDto searchCondition, Pageable pageable);
}
