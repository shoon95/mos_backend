package com.mos.backend.studyrequirements.entity;

import com.mos.backend.studies.entity.Study;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_requirements")
public class StudyRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_requirement_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Column(nullable = false)
    private Long requirementNum;

    @Column(nullable = false)
    private String content;

    public static StudyRequirement create(Study study, long requirementNum, String content) {
        StudyRequirement studyRequirement = new StudyRequirement();
        studyRequirement.study = study;
        studyRequirement.requirementNum = requirementNum;
        studyRequirement.content = content;
        return studyRequirement;
    }

    public void update(Long requirementNum, String content) {
        this.requirementNum = requirementNum;
        this.content = content;
    }
}
