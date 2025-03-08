package com.mos.backend.studybenefits.infrastructure;

import com.mos.backend.studybenefits.entity.StudyBenefit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyBenefitJpaRepository extends JpaRepository<StudyBenefit, Long> {
}