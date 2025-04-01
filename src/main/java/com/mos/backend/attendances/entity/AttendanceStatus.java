package com.mos.backend.attendances.entity;

import com.mos.backend.attendances.entity.exception.AttendanceErrorCode;
import com.mos.backend.common.exception.MosException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AttendanceStatus {
    PRESENT("출석", false),
    EXCUSED_ABSENCE("사유 결석", true),
    UNEXCUSED_ABSENCE("무단 결석", true),
    LATE("지각", false),
    EARLY_LEAVE("조퇴", true);

    private final String description;
    private final boolean isModifiable;

    public static AttendanceStatus fromDescription(String description) {
        return Arrays.stream(AttendanceStatus.values())
                .filter(a -> a.description.equals(description))
                .findFirst()
                .orElseThrow(() -> new MosException(AttendanceErrorCode.INVALID_ATTENDANCE_STATUS));
    }
}
