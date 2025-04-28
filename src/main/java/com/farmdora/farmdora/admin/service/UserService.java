package com.farmdora.farmdora.admin.service;

import com.farmdora.farmdora.admin.dto.UserSearchRequestDto;
import com.farmdora.farmdora.admin.dto.UserSearchResponseDto;
import com.farmdora.farmdora.admin.repository.UserRepository;
import com.farmdora.farmdora.common.response.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PageResponseDto<UserSearchResponseDto> searchUsers(UserSearchRequestDto searchCondition, Pageable pageable) {
        Page<UserSearchResponseDto> users = userRepository.searchUsers(searchCondition, pageable);
        return new PageResponseDto<>(users.getContent(), users);
    }
}
