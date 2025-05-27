package com.mos.backend.questionanswers.application;

import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.questionanswers.entity.QuestionAnswer;
import com.mos.backend.questionanswers.infrastructure.QuestionAnswerRepository;
import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studyquestions.entity.StudyQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class QuestionAnswerService {

    private final QuestionAnswerRepository questionAnswerRepository;
    private final EntityFacade entityFacade;

    @Transactional
    public void create(Long studyJoinId, Long studyQuestionId, String answer) {
        StudyJoin studyJoin = entityFacade.getStudyJoin(studyJoinId);
        StudyQuestion studyQuestion = entityFacade.getStudyQuestion(studyQuestionId);

        saveQuestionAnswer(answer, studyJoin, studyQuestion);
    }

    private void saveQuestionAnswer(String answer, StudyJoin studyJoin, StudyQuestion studyQuestion) {
        QuestionAnswer questionAnswer = new QuestionAnswer(studyJoin, studyQuestion, answer);
        questionAnswerRepository.save(questionAnswer);
    }
}
