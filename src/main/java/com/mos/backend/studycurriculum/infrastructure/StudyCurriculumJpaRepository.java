package com.mos.backend.studycurriculum.infrastructure;

import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyCurriculumJpaRepository extends JpaRepository<StudyCurriculum, Long> {
}