package com.mos.backend.questionanswers.infrastructure;

import com.mos.backend.questionanswers.entity.QuestionAnswer;
import com.mos.backend.studyjoins.application.res.QuestionAnswerRes;

import java.util.List;

public interface QuestionAnswerRepository {
    QuestionAnswer save(QuestionAnswer questionAnswer);
    List<QuestionAnswerRes> findAllByStudyJoinId(Long studyJoinId);
}
