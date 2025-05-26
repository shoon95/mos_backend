package com.mos.backend.studyjoins.entity.exception;

import com.mos.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@RequiredArgsConstructor
public enum StudyJoinErrorCode implements ErrorCode {
    STUDY_JOIN_NOT_FOUND(HttpStatus.NOT_FOUND, "study-join.not-found"),
    STUDY_JOIN_MISMATCH(HttpStatus.BAD_REQUEST, "study-join.mismatch"),
    STUDY_JOIN_NOT_PENDING(HttpStatus.BAD_REQUEST, "study-join.not-pending"),
    STUDY_JOIN_STATUS_NOT_FOUND(HttpStatus.BAD_REQUEST, "study-join.status.not-found"),
    MISSING_REQUIRED_QUESTIONS(HttpStatus.BAD_REQUEST, "study-join.missing-required-questions"),
    INVALID_ANSWER_OPTION(HttpStatus.BAD_REQUEST, "study-join.invalid-answer-option"),;

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
