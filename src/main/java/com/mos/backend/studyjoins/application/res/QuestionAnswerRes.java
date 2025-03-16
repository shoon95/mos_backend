package com.mos.backend.studyjoins.application.res;

import com.mos.backend.studyquestions.entity.QuestionOption;
import com.mos.backend.studyquestions.entity.QuestionType;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class QuestionAnswerRes {
    private Long studyQuestionId;
    private String question;
    private Long questionNum;
    private String questionType;
    private QuestionOption questionOption;

    private Long questionAnswerId;
    private String answer;

    public QuestionAnswerRes(Long studyQuestionId, String question, Long questionNum, QuestionType questionType, QuestionOption questionOption, Long questionAnswerId, String answer) {
        this.studyQuestionId = studyQuestionId;
        this.question = question;
        this.questionNum = questionNum;
        this.questionType = questionType.getDescription();
        this.questionOption = questionOption;
        this.questionAnswerId = questionAnswerId;
        this.answer = answer;
    }
}
