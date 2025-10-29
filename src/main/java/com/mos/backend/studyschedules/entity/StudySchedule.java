package com.mos.backend.studyschedules.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.studies.entity.Study;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_schedules")
public class StudySchedule extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_schedule_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "study_id")
    @OnDelete(action = CASCADE)
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
}
