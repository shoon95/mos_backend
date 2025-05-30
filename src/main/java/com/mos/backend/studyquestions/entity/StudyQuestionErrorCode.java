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
    INVALID_MULTIPLE_CHOICE_OPTIONS(HttpStatus.BAD_REQUEST, "study-question.invalid-multiple-choice-options"),
    STUDY_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "study-question.not-found"),
    INVALID_SHORT_ANSWER(HttpStatus.BAD_REQUEST, "study-question.invalid-short-answer"),
    INVALID_QUESTION_NUM(HttpStatus.BAD_REQUEST, "study-question.invalid-question-num"),
    STUDY_QUESTION_MISMATCH(HttpStatus.BAD_REQUEST, "study-question.mismatch");


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
