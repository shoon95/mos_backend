package com.mos.backend.studyrules.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyrules.entity.StudyRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyRuleJpaRepository extends JpaRepository<StudyRule, Long> {
    Optional<StudyRule> findByIdAndStudy(Long id, Study study);

    List<StudyRule> findAllByStudy(Study study);
}