package com.mos.backend.studyrules.infrastructure;

import com.mos.backend.studyrules.entity.StudyRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StudyRuleRepositoryImpl implements StudyRuleRepository{

    private final StudyRuleJpaRepository studyRuleJpaRepository;

    @Override
    public void save(StudyRule studyRule) {
        studyRuleJpaRepository.save(studyRule);
    }
}
