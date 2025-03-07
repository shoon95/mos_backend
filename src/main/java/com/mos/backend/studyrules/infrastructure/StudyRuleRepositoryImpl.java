package com.mos.backend.studyrules.infrastructure;

import com.mos.backend.studyrules.entity.StudyRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudyRuleRepositoryImpl implements StudyRuleRepository{

    private final StudyRuleJpaRepository studyRuleJpaRepository;

    @Override
    public void save(StudyRule studyRule) {
        studyRuleJpaRepository.save(studyRule);
    }

    @Override
    public void saveAll(List<StudyRule> studyRuleList) {
        studyRuleJpaRepository.saveAll(studyRuleList);
    }
}
