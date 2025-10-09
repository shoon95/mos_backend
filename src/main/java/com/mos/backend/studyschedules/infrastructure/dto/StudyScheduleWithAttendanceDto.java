package com.mos.backend.studyschedules.infrastructure.dto;

import com.mos.backend.attendances.entity.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StudyScheduleWithAttendanceDto {
    private Long studyScheduleId;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private boolean isCompleted;
    private Long studyId;
    private AttendanceStatus attendanceStatus;
}
