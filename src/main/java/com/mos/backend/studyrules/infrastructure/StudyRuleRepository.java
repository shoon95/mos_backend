package com.mos.backend.studyrules.infrastructure;

import com.mos.backend.studyrules.entity.StudyRule;

import java.util.List;

public interface StudyRuleRepository {
    void save(StudyRule studyRule);

    void saveAll(List<StudyRule> studyRuleList);
}
