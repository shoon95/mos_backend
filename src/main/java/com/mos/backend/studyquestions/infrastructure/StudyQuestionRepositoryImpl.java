package com.mos.backend.studyquestions.infrastructure;

import com.mos.backend.studyquestions.entity.StudyQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudyQuestionRepositoryImpl implements StudyQuestionRepository{

    private final StudyQuestionJpaRepository studyQuestionJpaRepository;

    @Override
    public void save(StudyQuestion studyQuestion) {
        studyQuestionJpaRepository.save(studyQuestion);
    }

    @Override
    public void saveAll(List<StudyQuestion> studyQuestionList) {
        studyQuestionJpaRepository.saveAll(studyQuestionList);
    }
}
