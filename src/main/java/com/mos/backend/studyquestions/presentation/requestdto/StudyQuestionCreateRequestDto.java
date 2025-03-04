package com.mos.backend.studyquestions.presentation.requestdto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudyQuestionCreateRequestDto {

    private String question;
    private boolean isRequired;
    private String answerType;
    private List<String> options;
}
