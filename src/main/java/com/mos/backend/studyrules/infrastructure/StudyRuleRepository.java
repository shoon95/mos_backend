package com.mos.backend.studyrules.infrastructure;

import com.mos.backend.studyrules.entity.StudyRule;

public interface StudyRuleRepository {
    void save(StudyRule studyRule);
}
