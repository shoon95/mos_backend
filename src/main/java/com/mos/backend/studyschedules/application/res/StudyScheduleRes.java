package com.mos.backend.studyschedules.application.res;

import com.mos.backend.studyschedules.entity.StudySchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StudyScheduleRes {
    private Long studyScheduleId;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private Long studyId;

    public static StudyScheduleRes from(StudySchedule studySchedule) {
        return new StudyScheduleRes(
                studySchedule.getId(),
                studySchedule.getTitle(),
                studySchedule.getDescription(),
                studySchedule.getStartTime(),
                studySchedule.getEndTime(),
                studySchedule.getStudy().getId()
        );
    }
}
