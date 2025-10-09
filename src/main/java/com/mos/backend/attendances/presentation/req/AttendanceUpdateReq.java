package com.mos.backend.attendances.presentation.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceUpdateReq {
    private String attendanceStatus;
}
