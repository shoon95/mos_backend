package com.mos.backend.studymaterials.entity;

import com.mos.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@RequiredArgsConstructor
@Getter
public enum FileUploadErrorCode implements ErrorCode {
    FILE_UPLOAD_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "file-upload.internal-server-error"),
    FILE_DELETE_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "file_delete.internal-server-error")
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
