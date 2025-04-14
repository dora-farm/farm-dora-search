package com.farmdora.farmdora.question.repository;

import com.farmdora.farmdora.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer>, CustomQuestionRepository {
}
