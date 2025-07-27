package com.mos.backend.userstudylikes.infrastructure;

import com.mos.backend.userstudylikes.application.response.LikesResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mos.backend.studies.entity.QStudy.study;
import static com.mos.backend.userstudylikes.entity.QUserStudyLike.userStudyLike;

@Repository
@RequiredArgsConstructor
public class UserStudyLikeQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Long getLikedCountByStudyId(Long studyId) {
        return jpaQueryFactory
                .select(userStudyLike.count())
                .from(userStudyLike)
                .where(
                        userStudyLike.study.id.eq(studyId)
                )
                .fetchOne();
    }

    public boolean isLikedByMe(Long studyId, Long currentUserId) {
        Integer fetchFirst = jpaQueryFactory
                .selectOne()
                .from(userStudyLike)
                .where(
                        userStudyLike.study.id.eq(studyId),
                        userStudyLike.user.id.eq(currentUserId)
                )
                .fetchFirst();
        return fetchFirst != null;
    }

    /**
     * 스터디 ID 목록으로 likedCount와 isLiked 조회
     */
    public List<LikesResponseDto> getLikes(List<Long> studyIds, Long currentUserId) {
        return jpaQueryFactory
                .select(Projections.constructor(LikesResponseDto.class,
                        study.id,
                        JPAExpressions
                                .select(userStudyLike.count())
                                .from(userStudyLike)
                                .where(userStudyLike.study.eq(study)),
                        isLikedByMe(currentUserId)))
                .from(study)
                .where(study.id.in(studyIds))
                .fetch();
    }

    private BooleanExpression isLikedByMe(Long currentUserId) {
        if (currentUserId == null) {
            return Expressions.asBoolean(true).isFalse();
        }

        return JPAExpressions
                .selectOne()
                .from(userStudyLike)
                .where(
                        userStudyLike.study.id.eq(study.id),
                        userStudyLike.user.id.eq(currentUserId)
                )
                .exists();
    }
}
