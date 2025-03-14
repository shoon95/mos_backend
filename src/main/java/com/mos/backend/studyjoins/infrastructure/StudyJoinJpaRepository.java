package com.mos.backend.studyjoins.infrastructure;

import com.mos.backend.studyjoins.entity.StudyJoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyJoinJpaRepository extends JpaRepository<StudyJoin, Long> {
}
