package com.mos.backend.studyrequirements.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyrequirements.entity.StudyRequirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyRequirementJpaRepository extends JpaRepository<StudyRequirement, Long> {
    List<StudyRequirement> findAllByStudy(Study study);

    StudyRequirement study(Study study);

    Optional<StudyRequirement> findByIdAndStudy(Long id, Study study);
}
