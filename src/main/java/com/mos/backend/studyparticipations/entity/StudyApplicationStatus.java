package com.mos.backend.studyparticipations.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyApplicationStatus {
    PENDING("대기"),
    REJECTED("탈락"),
    APPROVED("승낙"),
    ;

    private final String description;
}
