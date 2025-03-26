package com.mos.backend.attendances.infrastructure;

import com.mos.backend.attendances.entity.Attendance;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studyschedules.entity.StudySchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AttendanceRepositoryImpl implements AttendanceRepository {
    private final AttendanceJpaRepository attendanceJpaRepository;

    @Override
    public Attendance save(Attendance attendance) {
        return attendanceJpaRepository.save(attendance);
    }

    @Override
    public Optional<Attendance> findById(Long attendanceId) {
        return attendanceJpaRepository.findById(attendanceId);
    }

    @Override
    public List<Attendance> findAllByStudyMemberId(Long studyMemberId) {
        return attendanceJpaRepository.findAllByStudyMemberId(studyMemberId);
    }

    @Override
    public List<Attendance> findAllByStudyScheduleId(Long studyScheduleId) {
        return attendanceJpaRepository.findAllByStudyScheduleId(studyScheduleId);
    }

    @Override
    public Optional<Attendance> findByStudyScheduleAndStudyMember(StudySchedule studySchedule, StudyMember studyMember) {
        return attendanceJpaRepository.findByStudyScheduleAndStudyMember(studySchedule, studyMember);
    }

    @Override
    public void delete(Attendance attendance) {
        attendanceJpaRepository.delete(attendance);
    }
}
