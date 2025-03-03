package com.mos.backend.studymembers.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyParticipationStatus {

    ACTIVE("참여 중"),
    COMPLETED("완료"),
    WITHDRAWN("탈퇴");

    private final String description;
}
