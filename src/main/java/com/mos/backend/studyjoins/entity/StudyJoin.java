package com.mos.backend.studyjoins.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "study_joins")
public class StudyJoin extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_joins_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyJoinStatus status;

    public void approve() {
        this.status = StudyJoinStatus.APPROVED;
    }

    public void reject() {
        this.status = StudyJoinStatus.REJECTED;
    }

    public void cancel() {
        this.status = StudyJoinStatus.CANCELED;
    }

    public boolean isSameStudy(Study study) {
        return this.study.getId().equals(study.getId());
    }

    public boolean isPending() {
        return this.status == StudyJoinStatus.PENDING;
    }

    public static StudyJoin createPendingStudyJoin(User user, Study study) {
        return StudyJoin.builder()
                .user(user)
                .study(study)
                .status(StudyJoinStatus.PENDING)
                .build();
    }
}
