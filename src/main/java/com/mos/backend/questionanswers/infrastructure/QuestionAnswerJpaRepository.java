package com.mos.backend.questionanswers.infrastructure;

import com.mos.backend.questionanswers.entity.QuestionAnswer;
import com.mos.backend.studyjoins.application.res.QuestionAnswerRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionAnswerJpaRepository extends JpaRepository<QuestionAnswer, Long> {
    @Query("""
            SELECT new com.mos.backend.studyjoins.application.res.QuestionAnswerRes(sq.id, sq.question, sq.questionNum, sq.type, sq.options,qa.id ,qa.answer)
            FROM QuestionAnswer qa
            JOIN qa.studyQuestion sq
            WHERE qa.studyJoin.id = :studyJoinId
            ORDER BY sq.questionNum
            """)
    List<QuestionAnswerRes> findAllByStudyJoinId(Long studyJoinId);
}
