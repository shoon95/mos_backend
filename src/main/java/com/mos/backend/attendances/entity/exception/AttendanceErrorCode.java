package com.mos.backend.attendances.entity.exception;

import com.mos.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@RequiredArgsConstructor
public enum AttendanceErrorCode implements ErrorCode {
    ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "attendance.not-found"),
    ATTENDANCE_ALREADY_EXISTS(HttpStatus.CONFLICT, "attendance.already-exist"),
    INVALID_ATTENDANCE_STATUS(HttpStatus.BAD_REQUEST, "attendance.invalid-status"),
    UNRELATED_ATTENDANCE(HttpStatus.BAD_REQUEST, "attendance.unrelated"),
    NOT_PRESENT_TIME(HttpStatus.BAD_REQUEST, "attendance.not-present-time"),
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
