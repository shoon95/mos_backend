package com.mos.backend.userstudylikes.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
