package com.mos.backend.studyrules.entity.exception;

import com.mos.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@RequiredArgsConstructor
public enum StudyRuleErrorCode implements ErrorCode {
    STUDY_RULE_NOT_FOUND(HttpStatus.NOT_FOUND, "study-rule.not-found"),
    INVALID_RULE_NUM(HttpStatus.BAD_REQUEST, "study-rule.invalid-rule-num");

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
