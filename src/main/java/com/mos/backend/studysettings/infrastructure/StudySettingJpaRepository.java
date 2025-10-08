package com.mos.backend.studysettings.infrastructure;

import com.mos.backend.studysettings.entity.StudySettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudySettingJpaRepository extends JpaRepository<StudySettings, Long> {
    Optional<StudySettings> findByStudyId(Long studyId);
}