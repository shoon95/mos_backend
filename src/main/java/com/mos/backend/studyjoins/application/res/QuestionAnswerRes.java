package com.mos.backend.studyjoins.application.res;

import com.mos.backend.studyquestions.entity.QuestionOption;
import com.mos.backend.studyquestions.entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class QuestionAnswerRes {
    private Long studyQuestionId;
    private String question;
    private Long questionNum;
    private QuestionType questionType;
    private QuestionOption questionOption;

    private Long questionAnswerId;
    private String answer;
}
