package com.mos.backend.studyquestions.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.studies.entity.Study;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "study_questions")
public class StudyQuestion extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_question_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Column(nullable = false)
    private Long questionNum;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private QuestionType type;

    @Embedded
    private QuestionOption options;

    @Column(nullable = false)
    private boolean required;

    public static StudyQuestion create(Study study, Long questionNum, String question, String type, List<String> options, boolean required) {
        if ((QuestionType.MULTIPLE_CHOICE.equals(QuestionType.fromDescription(type))) && (options == null || options.size() < 2)) {
            throw new MosException(StudyQuestionErrorCode.INVALID_MULTIPLE_CHOICE_OPTIONS);
        }
        if ((QuestionType.SHORT_ANSWER.equals(QuestionType.fromDescription(type))) && (!options.isEmpty())) {
            throw new MosException(StudyQuestionErrorCode.INVALID_SHORT_ANSWER);
        }

        StudyQuestion studyQuestion = new StudyQuestion();
        studyQuestion.study = study;
        studyQuestion.questionNum = questionNum;
        studyQuestion.question = question;
        studyQuestion.type = QuestionType.fromDescription(type);
        studyQuestion.options = QuestionOption.fromList(options);
        studyQuestion.required = required;
        return studyQuestion;
    }

    public void changeQuestion(Long questionNum, String question, boolean required, String type, List<String> options) {
        changeQuestionNum(questionNum);
        changeQuestionContent(question);
        changeRequired(required);
        changeTypeAndOptions(type, options);
    }

    public void changeQuestionNum(Long questionNum) {
        this.questionNum = questionNum;
    }

    public void changeQuestionContent(String question) {
        this.question = question;
    }

    public void changeRequired(boolean required) {
        this.required = required;
    }

    public void changeTypeAndOptions(String type, List<String> options) {
        if ((QuestionType.MULTIPLE_CHOICE.equals(QuestionType.fromDescription(type))) && (options == null || options.size() < 2)) {
            throw new MosException(StudyQuestionErrorCode.INVALID_MULTIPLE_CHOICE_OPTIONS);
        }
        if ((QuestionType.SHORT_ANSWER.equals(QuestionType.fromDescription(type))) && (!options.isEmpty())) {
            throw new MosException(StudyQuestionErrorCode.INVALID_SHORT_ANSWER);
        }
        this.type = QuestionType.fromDescription(type);
        this.options = QuestionOption.fromList(options);
    }

    public boolean isSameStudy(Study study) {
        return this.study.getId().equals(study.getId());
    }
}
