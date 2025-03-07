package com.mos.backend.studies.infrastructure;

import com.mos.backend.studies.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyJpaRepository extends JpaRepository<Study, Long> {
}
