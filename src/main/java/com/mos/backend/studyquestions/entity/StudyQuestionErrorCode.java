package com.mos.backend.studyquestions.entity;

import com.mos.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@RequiredArgsConstructor
@Getter
public enum StudyQuestionErrorCode implements ErrorCode {
    INVALID_QUESTION_TYPE(HttpStatus.BAD_REQUEST, "study-question.invalid-question-type"),
    INVALID_MULTIPLE_CHOICE_OPTIONS(HttpStatus.BAD_REQUEST, "study-question.invalid-multiple-choice-options");

    private final HttpStatus httpStatus;
    private final String messageKey;

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorName() {
        return this.name();
    }

    @Override
    public String getMessage(MessageSource messageSource) {
        return messageSource.getMessage(messageKey, null, Locale.getDefault());
    }
}
