package com.mos.backend.studyquestions.entity;

import com.mos.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum StudyQuestionErrorCode implements ErrorCode {
    INVALID_QUESTION_TYPE(HttpStatus.BAD_REQUEST, "study-question.invalid-question-type"),
    ;

    private final HttpStatus httpStatus;
    private final String messageKey;

    @Override
    public HttpStatus getStatus() {
        return null;
    }

    @Override
    public String getErrorName() {
        return "";
    }

    @Override
    public String getMessage(MessageSource messageSource) {
        return "";
    }
}
