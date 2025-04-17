package com.farmdora.farmdora.question.service;

import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.question.dto.QuestionResponseDto;
import com.farmdora.farmdora.question.dto.QuestionSearchRequestDto;
import com.farmdora.farmdora.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public PageResponseDto<QuestionResponseDto> searchQuestions(Integer sellerId, QuestionSearchRequestDto searchCondition, Pageable pageable) {
        Page<QuestionResponseDto> questions = questionRepository.searchQuestions(sellerId, searchCondition, pageable);
        return new PageResponseDto<>(questions.getContent(), questions);
    }
}
