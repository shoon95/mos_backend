package com.mos.backend.questionanswers.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studyquestions.entity.StudyQuestion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "question_answers")
public class QuestionAnswer extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_answers_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_join_id", nullable = false)
    private StudyJoin studyJoin;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_question_id", nullable = false)
    private StudyQuestion studyQuestion;

    private String answer;

    public QuestionAnswer(StudyJoin studyJoin, StudyQuestion studyQuestion, String answer) {
        this.studyJoin = studyJoin;
        this.studyQuestion = studyQuestion;
        this.answer = answer;
    }
}
