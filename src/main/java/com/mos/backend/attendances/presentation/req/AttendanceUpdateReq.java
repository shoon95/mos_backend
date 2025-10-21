package com.mos.backend.attendances.presentation.req;

import com.mos.backend.attendances.entity.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceUpdateReq {
    private AttendanceStatus attendanceStatus;
}
