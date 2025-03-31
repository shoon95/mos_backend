package com.mos.backend.studies.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.studymaterials.application.UploadType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "studies", indexes = {
        @Index(name = "idx_category_created_at", columnList = "category, created_at"),
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_recruitment_status_category_created_at", columnList = "recruitment_status, category, created_at"),
        @Index(name = "idx_recruitment_status_created_at", columnList = "recruitment_status, created_at")
})
@Builder
@AllArgsConstructor
public class Study extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = true)
    private String notice;

    @Column(nullable = false)
    private Integer maxStudyMemberCount;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = true)
    private String schedule;

    @Column(nullable = false)
    private LocalDate recruitmentStartDate;

    @Column(nullable = false)
    private LocalDate recruitmentEndDate;

    @Embedded
    private StudyTag tags;

    @Column(nullable = false)
    @Builder.Default
    private int viewCount = 0;

    @Column(nullable = false)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingType meetingType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProgressStatus progressStatus = ProgressStatus.NOT_STARTED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RecruitmentStatus recruitmentStatus = RecruitmentStatus.OPEN;

    public boolean isRelated(Long studyId) {
        return this.id.equals(studyId);
    }
    public void changeImageToPermanent(Long userId, Long studyId) {
        content = content.replace(getOldChar(userId), getNewChar(studyId));
    }

    private String getOldChar(Long userId) {
        return UploadType.TEMP.getFolderPath() + "/" + userId;
    }

    private String getNewChar(Long studyId) {
        return UploadType.STUDY.getFolderPath() + "/" + studyId;
    }
}
