package com.mos.backend.studybenefits.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.studies.entity.Study;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_benefits")
public class StudyBenefit extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_benefit_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Column(nullable = false)
    private String content;

    public static StudyBenefit create(Study study, String content) {
        StudyBenefit studyBenefit = new StudyBenefit();
        studyBenefit.study = study;
        studyBenefit.content = content;
        return studyBenefit;
    }


}
