package com.mos.backend.studyschedules.infrastructure;

import com.mos.backend.studyschedules.entity.StudySchedule;
import com.mos.backend.studyschedules.infrastructure.dto.StudyScheduleWithAttendanceDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyScheduleJpaRepository extends JpaRepository<StudySchedule, Long> {
    @Query("""
                SELECT new com.mos.backend.studyschedules.infrastructure.dto.StudyScheduleWithAttendanceDto(
                    ss.id, ss.title, ss.description, ss.startDateTime, ss.endDateTime, ss.isCompleted, ss.study.id, a.attendanceStatus
                )
                FROM StudySchedule ss
                LEFT JOIN Attendance a ON a.studySchedule.id = ss.id AND a.studyMember.user.id = :userId
                WHERE ss.study.id = :studyId
            """)
    List<StudyScheduleWithAttendanceDto> findByStudyIdWithAttendance(Long userId, Long studyId);

    @Query("""
                SELECT new com.mos.backend.studyschedules.infrastructure.dto.StudyScheduleWithAttendanceDto(
                    ss.id, ss.title, ss.description, ss.startDateTime, ss.endDateTime, ss.isCompleted, ss.study.id, a.attendanceStatus
                )
                FROM StudySchedule ss
                JOIN ss.study s
                JOIN StudyMember sm ON sm.study = s
                LEFT JOIN Attendance a ON a.studySchedule.id = ss.id AND a.studyMember.user.id = :userId
                WHERE sm.user.id = :userId AND sm.status = 'ACTIVATED'
            """)
    List<StudyScheduleWithAttendanceDto> findAllByUserIdAndActivated(Long userId);

    List<StudySchedule> findByStudyId(Long studyId);
}
