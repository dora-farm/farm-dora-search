package com.farmdora.farmdora.question.controller;

import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_QUESTION_SUCCESS;

import com.farmdora.farmdora.common.response.HttpResponse;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.question.dto.QuestionResponseDto;
import com.farmdora.farmdora.question.dto.QuestionSearchRequestDto;
import com.farmdora.farmdora.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my/seller/order/inquiry")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<?> searchQuestions(Integer sellerId,
                                             QuestionSearchRequestDto searchCondition,
                                             @PageableDefault Pageable pageable) {
        PageResponseDto<QuestionResponseDto> questions = questionService.searchQuestions(sellerId, searchCondition, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_QUESTION_SUCCESS.getMessage(), questions));
    }
}
