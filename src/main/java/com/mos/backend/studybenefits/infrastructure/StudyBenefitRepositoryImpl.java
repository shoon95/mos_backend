package com.mos.backend.studybenefits.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studybenefits.entity.StudyBenefit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<StudyBenefit> findById(Long studyBenefitId) {
        return studyBenefitJpaRepository.findById(studyBenefitId);
    }

    @Override
    public Optional<StudyBenefit> findByIdAndStudy(Long id, Study study) {
        return studyBenefitJpaRepository.findByIdAndStudy(id, study);
    }

    @Override
    public List<StudyBenefit> findAllByStudy(Study study) {
        return studyBenefitJpaRepository.findAllByStudy(study);
    }

    @Override
    public void deleteAll(List<StudyBenefit> deleteBenefitList) {
        studyBenefitJpaRepository.deleteAll(deleteBenefitList);
    }
}
