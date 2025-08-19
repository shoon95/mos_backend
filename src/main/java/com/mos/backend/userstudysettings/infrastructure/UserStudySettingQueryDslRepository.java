package com.mos.backend.userstudysettings.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.mos.backend.userstudysettings.entity.QUserStudySetting.userStudySetting;

@RequiredArgsConstructor
@Repository
public class UserStudySettingQueryDslRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public void showNoticeForAllMembers(Long studyId) {
        long number = queryFactory
                .update(userStudySetting)
                .set(userStudySetting.noticePined, true)
                .where(userStudySetting.studyMember.study.id.eq(studyId))
                .execute();
        if (number > 0) {
            em.flush();
            em.clear();
        }
    }
}
