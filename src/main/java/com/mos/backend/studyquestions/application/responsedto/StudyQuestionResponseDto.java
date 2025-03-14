package com.mos.backend.studyquestions.application.responsedto;

import com.mos.backend.studyquestions.entity.StudyQuestion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StudyQuestionResponseDto {

    private Long id;
    private Long questionNum;
    private String question;
    private String type;
    private List<String> options;
    private boolean required;

    public static StudyQuestionResponseDto from(StudyQuestion studyQuestion) {
        StudyQuestionResponseDto responseDto = new StudyQuestionResponseDto();
        responseDto.id = studyQuestion.getId();
        responseDto.questionNum = studyQuestion.getQuestionNum();
        responseDto.question = studyQuestion.getQuestion();
        responseDto.type = studyQuestion.getType().getDescription();
        responseDto.options = studyQuestion.getOptions().toList();
        responseDto.required = studyQuestion.isRequired();
        return responseDto;
    }
}
