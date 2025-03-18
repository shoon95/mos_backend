package com.mos.backend.studyschedules.infrastructure;

import com.mos.backend.studyschedules.entity.StudySchedule;

import java.util.List;
import java.util.Optional;

public interface StudyScheduleRepository {
    StudySchedule save(StudySchedule studySchedule);

    List<StudySchedule> findByStudyId(Long studyId);

    List<StudySchedule> findAllByActivatedUserId(Long userId);

    Optional<StudySchedule> findById(Long studyScheduleId);

    void delete(StudySchedule studySchedule);
}
