package com.mos.backend.attendances.infrastructure;

import com.mos.backend.attendances.entity.Attendance;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studyschedules.entity.StudySchedule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceJpaRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByStudyScheduleAndStudyMember(StudySchedule studySchedule, StudyMember studyMember);

    @EntityGraph(attributePaths = {"studySchedule"})
    List<Attendance> findAllByStudyMemberId(Long studyMemberId);

    List<Attendance> findAllByStudyScheduleId(Long studyScheduleId);
}
