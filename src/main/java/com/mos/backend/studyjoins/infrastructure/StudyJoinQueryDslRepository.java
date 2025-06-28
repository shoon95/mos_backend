package com.mos.backend.studyjoins.infrastructure;

import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studyjoins.entity.StudyJoinStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mos.backend.studyjoins.entity.QStudyJoin.studyJoin;

@Repository
@RequiredArgsConstructor
public class StudyJoinQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public List<StudyJoin> findAllByUserIdAndStatus(Long userId, StudyJoinStatus status) {
        return queryFactory
                .selectFrom(studyJoin)
                .join(studyJoin.study).fetchJoin()
                .where(eqUserId(userId))
                .where(eqStatus(status))
                .fetch();
    }

    public List<StudyJoin> findAllByStudyIdAndStatus(Long studyId, StudyJoinStatus status) {
        return queryFactory
                .selectFrom(studyJoin)
                .join(studyJoin.study).fetchJoin()
                .where(eqStudyId(studyId))
                .where(eqStatus(status))
                .fetch();
    }

    private static BooleanExpression eqStudyId(Long studyId) {
        return studyJoin.study.id.eq(studyId);
    }

    private static BooleanExpression eqUserId(Long userId) {
        return studyJoin.user.id.eq(userId);
    }

    private static BooleanExpression eqStatus(StudyJoinStatus status) {
        return status == null ? null : studyJoin.status.eq(status);
    }
}
