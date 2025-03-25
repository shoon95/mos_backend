package com.mos.backend.attendances.application.res;

import com.mos.backend.studyschedules.entity.StudySchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StudyScheduleAttendanceRateRes {
    private Long studyScheduleId;
    private LocalDateTime studyScheduleStartDateTime;
    private double attendanceRate;

    public static StudyScheduleAttendanceRateRes of(StudySchedule studySchedule, double attendanceRate) {
        return new StudyScheduleAttendanceRateRes(studySchedule.getId(), studySchedule.getStartDateTime(), attendanceRate);
    }
}
