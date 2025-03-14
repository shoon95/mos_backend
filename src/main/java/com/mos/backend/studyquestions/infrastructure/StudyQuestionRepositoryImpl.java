package com.mos.backend.studyquestions.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyquestions.entity.StudyQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<StudyQuestion> findAllByStudy(Study study) {
        return studyQuestionJpaRepository.findAllByStudy(study);
    }

    @Override
    public void deleteAll(List<StudyQuestion> deleteQuestionList) {
        studyQuestionJpaRepository.deleteAll(deleteQuestionList);
    }

    @Override
    public Optional<StudyQuestion> findByIdAndStudy(long questionId, Study study) {
        return studyQuestionJpaRepository.findByIdAndStudy(questionId, study);
    }
}
