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
    private Long benefitNum;

    @Column(nullable = false)
    private String content;

    public static StudyBenefit create(Study study, Long benefitNum, String content) {
        StudyBenefit studyBenefit = new StudyBenefit();
        studyBenefit.study = study;
        studyBenefit.benefitNum = benefitNum;
        studyBenefit.content = content;
        return studyBenefit;
    }

    public void changeNumAndContent(Long benefitNum, String content) {
        changeBenefitNum(benefitNum);
        changeContent(content);
    }

    public void changeBenefitNum(Long benefitNum) {
        this.benefitNum = benefitNum;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
