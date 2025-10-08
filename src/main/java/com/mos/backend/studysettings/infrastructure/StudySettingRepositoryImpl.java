package com.mos.backend.studysettings.infrastructure;

import com.mos.backend.studysettings.entity.StudySettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class StudySettingRepositoryImpl implements StudySettingRepository{
    private final StudySettingJpaRepository studySettingJpaRepository;

    @Override
    public void save(StudySettings studySettings) {
        studySettingJpaRepository.save(studySettings);
    }

    @Override
    public Optional<StudySettings> findByStudyId(Long studyId) {
        return studySettingJpaRepository.findByStudyId(studyId);
    }
}