package com.mos.backend.studybenefits.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studybenefits.entity.StudyBenefit;

import java.util.List;
import java.util.Optional;

public interface StudyBenefitRepository {
    void save(StudyBenefit studyBenefit);

    void saveAll(List<StudyBenefit> studyBenefitList);

    Optional<StudyBenefit> findById(Long studyBenefitId);

    Optional<StudyBenefit> findByIdAndStudy(Long id, Study study);

    List<StudyBenefit> findAllByStudy(Study study);

    void deleteAll(List<StudyBenefit> deleteBenefitList);
}
