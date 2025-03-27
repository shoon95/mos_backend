package com.mos.backend.studymembers.entity.exception;

import com.mos.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@RequiredArgsConstructor
public enum StudyMemberErrorCode implements ErrorCode {
    STUDY_MEMBER_FULL(HttpStatus.BAD_REQUEST, "study-member.full"),
    STUDY_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "study-member.not-found"),
    STUDY_LEADER_ALREADY_EXIST(HttpStatus.CONFLICT, "study-member.leader-already-exist")
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
