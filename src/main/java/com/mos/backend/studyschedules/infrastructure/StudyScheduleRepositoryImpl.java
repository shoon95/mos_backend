package com.mos.backend.studyschedules.infrastructure;

import com.mos.backend.studyschedules.entity.StudySchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyScheduleRepositoryImpl implements StudyScheduleRepository {

    private final StudyScheduleJpaRepository studyScheduleJpaRepository;

    @Override
    public StudySchedule save(StudySchedule studySchedule) {
        return studyScheduleJpaRepository.save(studySchedule);
    }

    @Override
    public List<StudySchedule> findByStudyId(Long studyId) {
        return studyScheduleJpaRepository.findByStudyId(studyId);
    }

    @Override
    public List<StudySchedule> findAllByActivatedUserId(Long userId) {
        return studyScheduleJpaRepository.findAllByActivatedUserId(userId);
    }

    @Override
    public Optional<StudySchedule> findById(Long studyScheduleId) {
        return studyScheduleJpaRepository.findById(studyScheduleId);
    }

    @Override
    public void delete(StudySchedule studySchedule) {
        studyScheduleJpaRepository.delete(studySchedule);
    }
}
