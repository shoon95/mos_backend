package com.mos.backend.studyschedules.infrastructure;

import com.mos.backend.studyschedules.entity.StudySchedule;
import com.mos.backend.studyschedules.infrastructure.dto.StudyScheduleWithAttendanceDto;
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
    public List<StudyScheduleWithAttendanceDto> findByStudyIdWithAttendance(Long userId, Long studyId) {
        return studyScheduleJpaRepository.findByStudyIdWithAttendance(userId, studyId);
    }

    @Override
    public List<StudyScheduleWithAttendanceDto> findAllByActivatedUserId(Long userId) {
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

    @Override
    public List<StudySchedule> findByStudyId(Long studyId) {
        return studyScheduleJpaRepository.findByStudyId(studyId);
    }
}
