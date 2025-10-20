package com.mos.backend.studysettings.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.studies.entity.Study;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_settings")
public class StudySettings extends BaseAuditableEntity {

    public static final int DEFAULT_LATE_THRESHOLD_MINUTES = 15;
    public static final int DEFAULT_ABSENCE_THRESHOLD_MINUTES = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "study_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Study study;

    @Column(nullable = false)
    private Integer lateThresholdMinutes;

    @Column(nullable = false)
    private Integer absenceThresholdMinutes;

    public static StudySettings create(Study study) {
        StudySettings studySettings = new StudySettings();
        studySettings.study = study;
        studySettings.lateThresholdMinutes = DEFAULT_LATE_THRESHOLD_MINUTES;
        studySettings.absenceThresholdMinutes = DEFAULT_ABSENCE_THRESHOLD_MINUTES;
        return studySettings;
    }

    public void update(Integer lateThresholdMinutes, Integer absenceThresholdMinutes) {
        this.lateThresholdMinutes = lateThresholdMinutes;
        this.absenceThresholdMinutes = absenceThresholdMinutes;
    }
}
