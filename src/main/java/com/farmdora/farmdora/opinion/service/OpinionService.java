package com.farmdora.farmdora.opinion.service;

import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.opinion.dto.QuestionResponseDto;
import com.farmdora.farmdora.opinion.dto.QuestionSearchRequestDto;
import com.farmdora.farmdora.opinion.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OpinionService {
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public PageResponseDto<QuestionResponseDto> searchQuestions(Integer sellerId, QuestionSearchRequestDto searchCondition, Pageable pageable) {
        Page<QuestionResponseDto> questions = questionRepository.searchQuestions(sellerId, searchCondition, pageable);
        return new PageResponseDto<>(questions.getContent(), questions);
    }
}
