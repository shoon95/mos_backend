package com.mos.backend.studymaterials.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studymembers.entity.StudyMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_materials")
public class StudyMaterial extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_material_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_member_id", nullable = false)
    private StudyMember studyMember;

    @Lob
    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private Long fileSize;

    public static StudyMaterial create(Study study, StudyMember studyMember, String filePath, String originalName, Long fileSize) {
        StudyMaterial studyMaterial = new StudyMaterial();
        studyMaterial.study = study;
        studyMaterial.studyMember = studyMember;
        studyMaterial.filePath = filePath;
        studyMaterial.originalName = originalName;
        studyMaterial.fileSize = fileSize;
        return studyMaterial;
    }
}
