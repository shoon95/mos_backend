package com.mos.backend.studybenefits.entity.exception;

import com.mos.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@RequiredArgsConstructor
public enum StudyBenefitErrorCode implements ErrorCode {
    STUDY_BENEFIT_NOT_FOUND(HttpStatus.NOT_FOUND, "study.benefit.not-found"),
    INVALID_BENEFIT_NUM(HttpStatus.BAD_REQUEST, "study.benefit.invalid-benefit-num");

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
