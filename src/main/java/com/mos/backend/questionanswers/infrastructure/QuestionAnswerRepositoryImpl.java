package com.mos.backend.questionanswers.infrastructure;

import com.mos.backend.questionanswers.entity.QuestionAnswer;
import com.mos.backend.studyjoins.application.res.QuestionAnswerRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionAnswerRepositoryImpl implements QuestionAnswerRepository {
    private final QuestionAnswerJpaRepository questionAnswerJpaRepository;

    @Override
    public QuestionAnswer save(QuestionAnswer questionAnswer) {
        return questionAnswerJpaRepository.save(questionAnswer);
    }

    @Override
    public List<QuestionAnswerRes> findAllByStudyJoinId(Long studyJoinId) {
        return questionAnswerJpaRepository.findAllByStudyJoinId(studyJoinId);
    }
}
