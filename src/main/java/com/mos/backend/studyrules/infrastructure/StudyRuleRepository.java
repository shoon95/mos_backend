package com.mos.backend.studyrules.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyrules.entity.StudyRule;

import java.util.List;
import java.util.Optional;

public interface StudyRuleRepository {
    void save(StudyRule studyRule);

    void saveAll(List<StudyRule> studyRuleList);

    Optional<StudyRule> findByIdAndStudy(Long id, Study study);

    List<StudyRule> findAllByStudy(Study study);

    void deleteAll(List<StudyRule> deleteRuleList);
}
