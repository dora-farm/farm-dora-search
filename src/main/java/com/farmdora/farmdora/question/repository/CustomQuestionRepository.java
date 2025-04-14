package com.farmdora.farmdora.question.repository;

import com.farmdora.farmdora.question.dto.QuestionResponseDto;
import com.farmdora.farmdora.question.dto.QuestionSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomQuestionRepository {
    Page<QuestionResponseDto> searchQuestions(Integer sellerId, QuestionSearchRequestDto searchCondition, Pageable pageable);
}
