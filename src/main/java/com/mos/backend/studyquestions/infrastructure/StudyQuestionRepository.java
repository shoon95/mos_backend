package com.mos.backend.studyquestions.infrastructure;

import com.mos.backend.studyquestions.entity.StudyQuestion;

import java.util.List;

public interface StudyQuestionRepository{
    void save(StudyQuestion studyQuestion);

    void saveAll(List<StudyQuestion> studyQuestionList);
}
