package com.mos.backend.attendances.infrastructure;

import com.mos.backend.attendances.entity.Attendance;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studyschedules.entity.StudySchedule;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository {
    Attendance save(Attendance attendance);

    Optional<Attendance> findByStudyScheduleAndStudyMember(StudySchedule studySchedule, StudyMember studyMember);

    void delete(Attendance attendance);

    Optional<Attendance> findById(Long attendanceId);

    List<Attendance> findAllByStudyMemberId(Long studyMemberId);

    List<Attendance> findAllByStudyScheduleId(Long studyScheduleId);
}
