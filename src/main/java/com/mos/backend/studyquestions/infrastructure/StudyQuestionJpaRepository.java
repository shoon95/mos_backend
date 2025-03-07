package com.mos.backend.studyquestions.infrastructure;

import com.mos.backend.studyquestions.entity.StudyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyQuestionJpaRepository extends JpaRepository<StudyQuestion, Long> {
}