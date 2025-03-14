package com.mos.backend.studybenefits.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studybenefits.entity.StudyBenefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyBenefitJpaRepository extends JpaRepository<StudyBenefit, Long> {
    Optional<StudyBenefit> findByIdAndStudy(Long id, Study study);

    List<StudyBenefit> findAllByStudy(Study study);
}