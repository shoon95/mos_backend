package com.mos.backend.studybenefits.infrastructure;

import com.mos.backend.studybenefits.entity.StudyBenefit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudyBenefitRepositoryImpl implements StudyBenefitRepository{

    private final StudyBenefitJpaRepository studyBenefitJpaRepository;

    @Override
    public void save(StudyBenefit studyBenefit) {
        studyBenefitJpaRepository.save(studyBenefit);
    }

    @Override
    public void saveAll(List<StudyBenefit> studyBenefitList) {
        studyBenefitJpaRepository.saveAll(studyBenefitList);
    }
}
