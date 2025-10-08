package com.mos.backend.studysettings.application.res;

import com.mos.backend.studysettings.entity.StudySettings;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudySettingRes {
    private Long studySettingId;
    private Long studyId;
    private Integer lateThresholdMinutes;
    private Integer absenceThresholdMinutes;

    public static StudySettingRes of(StudySettings studySettings, Long studyId) {
        return new StudySettingRes(
                studySettings.getId(),
                studyId,
                studySettings.getLateThresholdMinutes(),
                studySettings.getAbsenceThresholdMinutes()
        );
    }
}
