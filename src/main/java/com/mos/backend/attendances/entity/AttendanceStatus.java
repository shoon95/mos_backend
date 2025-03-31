package com.mos.backend.attendances.entity;

import com.mos.backend.attendances.entity.exception.AttendanceErrorCode;
import com.mos.backend.common.exception.MosException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AttendanceStatus {
    PRESENT("출석"),
    EXCUSED_ABSENCE("사유 결석"),
    UNEXCUSED_ABSENCE("무단 결석"),
    LATE("지각"),
    EARLY_LEAVE("조퇴");

    private final String description;

    public static AttendanceStatus fromDescription(String description) {
        return Arrays.stream(AttendanceStatus.values())
                .filter(a -> a.description.equals(description))
                .findFirst().orElseThrow(() -> new MosException(AttendanceErrorCode.INVALID_ATTENDANCE_STATUS));
    }
}
