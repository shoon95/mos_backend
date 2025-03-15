package com.mos.backend.studyquestions.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyquestions.entity.StudyQuestion;

import java.util.List;
import java.util.Optional;

public interface StudyQuestionRepository{
    StudyQuestion save(StudyQuestion studyQuestion);

    void saveAll(List<StudyQuestion> studyQuestionList);

    List<StudyQuestion> findAllByStudy(Study study);

    void deleteAll(List<StudyQuestion> deleteQuestionList);

    Optional<StudyQuestion> findByIdAndStudy(long questionId, Study study);
}
