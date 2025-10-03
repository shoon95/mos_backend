package com.mos.backend.studysettings.infrastructure;

import com.mos.backend.studysettings.entity.StudySettings;

import java.util.Optional;

public interface StudySettingRepository {

    void save(StudySettings studySettings);

    Optional<StudySettings> findByStudyId(Long studyId);
}