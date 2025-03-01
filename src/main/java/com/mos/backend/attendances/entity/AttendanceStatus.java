package com.mos.backend.attendances.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttendanceStatus {
    PRESENT("출석"),
    EXCUSED_ABSENCE("사유 결석"),
    UNEXCUSED_ABSENCE("무단 결석"),
    LATE("지각"),
    EARLY_LEAVE("조퇴");

    private final String description;
}
