package com.mos.backend.studyquestions.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyquestions.entity.StudyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyQuestionJpaRepository extends JpaRepository<StudyQuestion, Long> {
    List<StudyQuestion> findByStudyIdAndRequiredTrue(Long studyId);
    List<StudyQuestion> findAllByStudy(Study study);

    Optional<StudyQuestion> findByIdAndStudy(long questionId, Study study);
}
