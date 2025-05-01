package com.farmdora.farmdora.admin.controller;

import static com.farmdora.farmdora.common.response.SuccessMessage.GET_POPUPS_SUCCESS;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.farmdora.farmdora.ControllerTest;
import com.farmdora.farmdora.admin.dto.PopupSearchRequestDto;
import com.farmdora.farmdora.admin.dto.PopupSearchResponseDto;
import com.farmdora.farmdora.common.response.PageResponseDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class PopupControllerTest extends ControllerTest {

    @Test
    @DisplayName("팝업 목록 검색 API 테스트")
    void testSearchPopups() throws Exception {
        // given
        List<PopupSearchResponseDto> popups = List.of(
                PopupSearchResponseDto.builder()
                        .title("popup1")
                        .imageUrl("image")
                        .build(),
                PopupSearchResponseDto.builder()
                        .title("popup2")
                        .imageUrl("image")
                        .build()
        );
        PageResponseDto<PopupSearchResponseDto> pageResponseDto = new PageResponseDto<>();
        pageResponseDto.setContents(popups);
        pageResponseDto.setTotalElements(2L);

        Pageable pageable = PageRequest.of(0, 10);
        when(popupService.searchPopups(any(PopupSearchRequestDto.class), any(Pageable.class))).thenReturn(pageResponseDto);

        // when
        // then
        mvc.perform(get("/admin/popup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(200)))
                .andExpect(jsonPath("$.message", equalTo(GET_POPUPS_SUCCESS.getMessage())));
    }
}