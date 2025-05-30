package com.mos.backend.studyjoins.application.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mos.backend.studyquestions.entity.QuestionOption;
import com.mos.backend.studyquestions.entity.QuestionType;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class QuestionAnswerRes {
    private Long studyQuestionId;
    private String question;
    private Long questionNum;
    private String questionType;
    private List<String> questionOptions;

    private Long questionAnswerId;
    private String answer;

    public QuestionAnswerRes(Long studyQuestionId, String question, Long questionNum, QuestionType questionType, QuestionOption questionOption, Long questionAnswerId, String answer) {
        this.studyQuestionId = studyQuestionId;
        this.question = question;
        this.questionNum = questionNum;
        this.questionType = questionType.getDescription();
        this.questionOptions = questionOption == null ? null : questionOption.toList();
        this.questionAnswerId = questionAnswerId;
        this.answer = answer;
    }
}
