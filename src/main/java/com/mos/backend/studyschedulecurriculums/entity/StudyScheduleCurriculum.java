package com.mos.backend.studyschedulecurriculums.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import com.mos.backend.studyschedules.entity.StudySchedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_schedule_curriculums")
public class StudyScheduleCurriculum extends BaseAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_schedule_curriculum_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "study_schedule_id")
//    @OnDelete(action = CASCADE)
    private StudySchedule studySchedule;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "study_curriculum_id")
    @OnDelete(action = CASCADE)
    private StudyCurriculum studyCurriculum;


    public static StudyScheduleCurriculum create(StudySchedule studySchedule, StudyCurriculum studyCurriculum) {
        StudyScheduleCurriculum studyScheduleCurriculum = new StudyScheduleCurriculum();
        studyScheduleCurriculum.studySchedule = studySchedule;
        studyScheduleCurriculum.studyCurriculum = studyCurriculum;
        return studyScheduleCurriculum;
    }
}
