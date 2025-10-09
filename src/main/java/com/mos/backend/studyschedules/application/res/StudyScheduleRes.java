package com.mos.backend.studyschedules.application.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class StudyScheduleRes {
    private Long studyScheduleId;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private Long studyId;

    private String attendanceStatusDescription;

    private List<StudyCurriculumRes> studyCurriculumResList;

    public static StudyScheduleRes of(Long studyScheduleId,
                                      String title,
                                      String description,
                                      LocalDateTime startDateTime,
                                      LocalDateTime endDateTime,
                                      Long studyId,
                                      String attendanceStatusDescription,
                                      List<StudyCurriculumRes> studyCurriculumList) {
        return new StudyScheduleRes(
                studyScheduleId,
                title,
                description,
                startDateTime,
                endDateTime,
                studyId,
                attendanceStatusDescription,
                studyCurriculumList
        );
    }
}
