package com.farmdora.farmdora.opinion.controller;

import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_QUESTION_SUCCESS;
import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_REVIEWS_SUCCESS;

import com.farmdora.farmdora.common.response.HttpResponse;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.opinion.dto.QuestionResponseDto;
import com.farmdora.farmdora.opinion.dto.OpinionSearchRequestDto;
import com.farmdora.farmdora.opinion.dto.ReviewResponseDto;
import com.farmdora.farmdora.opinion.service.OpinionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/my/seller/order")
@RequiredArgsConstructor
public class OpinionController {
    private final OpinionService opinionService;

    @GetMapping("/inquiry")
    public ResponseEntity<?> searchQuestions(Integer sellerId,
                                             OpinionSearchRequestDto searchCondition,
                                             @PageableDefault Pageable pageable) {
        PageResponseDto<QuestionResponseDto> questions = opinionService.searchQuestions(sellerId, searchCondition, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_QUESTION_SUCCESS.getMessage(), questions));
    }

    @GetMapping("/review")
    public ResponseEntity<?> searchReviews(OpinionSearchRequestDto searchCondition,
                                           @PageableDefault Pageable pageable) {
        PageResponseDto<ReviewResponseDto> reviews = opinionService.searchReviews(searchCondition, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_REVIEWS_SUCCESS.getMessage(), reviews));
    }
}
