package com.mos.backend.studyrequirements.entity.exception;

import com.mos.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@RequiredArgsConstructor
public enum StudyRequirementErrorCode implements ErrorCode {
    STUDY_REQUIREMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "study-requirement.not-found"),
    INVALID_REQUIREMENT_NUM(HttpStatus.BAD_REQUEST, "study-requirement.invalid-requirement-num");

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
