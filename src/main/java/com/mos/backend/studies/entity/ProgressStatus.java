package com.mos.backend.studies.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProgressStatus {

    NOT_STARTED("진행 전"),
    IN_PROGRESS("진행 중"),
    COMPLETED("완료"),
    PAUSED("정지"),
    FAILED("실패");

    private final String description;
}
