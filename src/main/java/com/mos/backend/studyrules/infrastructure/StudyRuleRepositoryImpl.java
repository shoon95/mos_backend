package com.mos.backend.studyrules.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyrules.entity.StudyRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<StudyRule> findByIdAndStudy(Long id, Study study) {
        return studyRuleJpaRepository.findByIdAndStudy(id, study);
    }

    @Override
    public List<StudyRule> findAllByStudy(Study study) {
        return studyRuleJpaRepository.findAllByStudy(study);
    }

    @Override
    public void deleteAll(List<StudyRule> deleteRuleList) {
        studyRuleJpaRepository.deleteAll(deleteRuleList);
    }
}
