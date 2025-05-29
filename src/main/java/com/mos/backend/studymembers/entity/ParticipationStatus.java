package com.mos.backend.studymembers.entity;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studymembers.entity.exception.StudyMemberErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ParticipationStatus {

    ACTIVATED("참여 중"),
    COMPLETED("완료"),
    WITHDRAWN("탈퇴");

    private final String description;

    public static ParticipationStatus fromDescription(String description) {
        return Arrays.stream(ParticipationStatus.values())
                .filter(p -> p.description.equals(description))
                .findFirst().orElseThrow(()-> new MosException(StudyMemberErrorCode.INVALID_PARTICIPATION_STATUS));
    }
}
