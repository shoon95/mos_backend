package com.mos.backend.studyrules.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.studies.entity.Study;
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
@Table(name = "study_rules")
public class StudyRule extends BaseAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_rule_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    @OnDelete(action = CASCADE)
    private Study study;

    @Column(nullable = false)
    private Long ruleNum;

    @Column(nullable = false)
    private String content;

    public static StudyRule create(Study study, Long ruleNum, String content) {
        StudyRule studyRule = new StudyRule();
        studyRule.study = study;
        studyRule.ruleNum = ruleNum;
        studyRule.content = content;
        return studyRule;
    }

    public void update(Long ruleNum, String content) {
        this.ruleNum = ruleNum;
        this.content = content;
    }

}
