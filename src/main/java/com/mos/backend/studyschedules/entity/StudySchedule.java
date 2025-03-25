package com.mos.backend.studyschedules.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.studies.entity.Study;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_schedules")
public class StudySchedule extends BaseAuditableEntity {

    private static final int PRESENT_TIME = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_schedule_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "study_id")
    private Study study;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = true)
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    private boolean isCompleted;

    public static StudySchedule create(Study study, String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        StudySchedule studySchedule = new StudySchedule();
        studySchedule.study = study;
        studySchedule.title = title;
        studySchedule.description = description;
        studySchedule.startDateTime = startTime;
        studySchedule.endDateTime = endTime;
        studySchedule.isCompleted = false;
        return studySchedule;
    }

    public void update(String title, String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public void complete() {
        this.isCompleted = true;
    }

    public boolean isPresentTime() {
        return LocalDateTime.now().isAfter(startDateTime.minusMinutes(PRESENT_TIME))
                && LocalDateTime.now().isBefore(startDateTime.plusMinutes(PRESENT_TIME));
    }

    public boolean isBeforePresentTime() {
        return LocalDateTime.now().isBefore(startDateTime.minusMinutes(PRESENT_TIME));
    }
}
