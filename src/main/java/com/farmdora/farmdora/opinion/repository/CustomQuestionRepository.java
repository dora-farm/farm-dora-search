package com.farmdora.farmdora.opinion.repository;

import com.farmdora.farmdora.opinion.dto.QuestionResponseDto;
import com.farmdora.farmdora.opinion.dto.QuestionSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomQuestionRepository {
    Page<QuestionResponseDto> searchQuestions(Integer sellerId, QuestionSearchRequestDto searchCondition, Pageable pageable);
}
