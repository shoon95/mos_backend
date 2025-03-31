package com.mos.backend.studymaterials.entity;

import com.mos.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@RequiredArgsConstructor
@Getter
public enum StudyMaterialErrorCode implements ErrorCode {
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "study-material.file-size-exceeded"),
    TOTAL_STUDY_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "study-material.total-study-size-exceeded"),
    STUDY_MATERIAL_NOT_FOUND(HttpStatus.NOT_FOUND, "study-material.not-found"),
    STUDY_NOT_MATCH(HttpStatus.BAD_REQUEST, "study-material.study-not-match")
    ;

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
