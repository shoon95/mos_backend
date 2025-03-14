package com.mos.backend.studyjoins.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyJoinStatus {
    PENDING("대기"),
    REJECTED("탈락"),
    APPROVED("승낙"),
    ;

    private final String description;
}
