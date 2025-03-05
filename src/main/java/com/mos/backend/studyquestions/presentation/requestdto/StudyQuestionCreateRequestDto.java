package com.mos.backend.studyquestions.presentation.requestdto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudyQuestionCreateRequestDto {

    private String question;
    private Long questionId;
    private boolean isRequired;
    private String type;
    private List<String> options;
}
