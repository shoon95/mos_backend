package com.mos.backend.studybenefits.infrastructure;

import com.mos.backend.studybenefits.entity.StudyBenefit;

import java.util.List;

public interface StudyBenefitRepository {
    void save(StudyBenefit studyBenefit);

    void saveAll(List<StudyBenefit> studyBenefitList);
}
