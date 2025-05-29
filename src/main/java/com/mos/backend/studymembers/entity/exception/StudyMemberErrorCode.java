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
    STUDY_LEADER_ALREADY_EXIST(HttpStatus.CONFLICT, "study-member.leader-already-exist"),
    STUDY_LEADER_WITHDRAW_FORBIDDEN(HttpStatus.FORBIDDEN, "study-member.leader-withdraw-forbidden"),
    INVALID_STUDY_MEMBER_ROLE_TYPE(HttpStatus.BAD_REQUEST, "study-member.invalid-role-type"),
    ONLY_LEADER_CAN_DELEGATE(HttpStatus.FORBIDDEN, "study-member.only-leader-can-delegate"),
    INVALID_PARTICIPATION_STATUS(HttpStatus.BAD_REQUEST, "study-member.invalid-participation-status");


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
