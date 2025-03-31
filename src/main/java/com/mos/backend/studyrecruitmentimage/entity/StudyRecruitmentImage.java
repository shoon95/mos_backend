package com.mos.backend.studyrecruitmentimage.entity;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studymaterials.application.UploadType;
import com.mos.backend.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_recruitment_images")
public class StudyRecruitmentImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_recruitment_image_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_id", nullable = true)
    private Study study;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private Long fileSize;

    public static StudyRecruitmentImage create(User user, String filePath, String originalName, Long fileSize) {
        StudyRecruitmentImage studyRecruitmentImage = new StudyRecruitmentImage();
        studyRecruitmentImage.user = user;
        studyRecruitmentImage.filePath = filePath;
        studyRecruitmentImage.originalName = originalName;
        studyRecruitmentImage.fileSize = fileSize;
        return studyRecruitmentImage;
    }

    public void changeToPermanent(Long userId, Study study) {
        this.study = study;
        filePath = filePath.replace(getOldChar(userId), getNewChar(study.getId()));
    }

    private String getOldChar(Long userId) {
        return UploadType.TEMP.getFolderPath() + "/" + userId;
    }

    private String getNewChar(Long studyId) {
        return UploadType.STUDY.getFolderPath() + "/" + studyId;
    }
}
