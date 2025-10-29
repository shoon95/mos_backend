package com.mos.backend.studynotices.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.users.entity.User;
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
@Table(name = "study_notices")
public class StudyNotice extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_notice_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    @OnDelete(action = CASCADE)
    private Study study;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean pinned;

    @Column(nullable = false)
    private boolean important;

    public static StudyNotice create(Study study, User user, String title, String content, boolean pinned, boolean important) {
        StudyNotice studyNotice = new StudyNotice();
        studyNotice.study = study;
        studyNotice.user = user;
        studyNotice.title = title;
        studyNotice.content = content;
        studyNotice.pinned = pinned;
        studyNotice.important = important;
        return studyNotice;
    }

    public void  update(String title, String content, boolean pinned, boolean important) {
        this.title = title;
        this.content = content;
        this.pinned = pinned;
        this.important = important;
    }

    public void unmarkAsImportant() {
        this.important = false;
    }

}
