package com.mos.backend.attendances.application;

import com.mos.backend.attendances.application.res.StudyMemberAttendanceRes;
import com.mos.backend.attendances.application.res.StudyScheduleAttendanceRateRes;
import com.mos.backend.attendances.entity.Attendance;
import com.mos.backend.attendances.entity.AttendanceStatus;
import com.mos.backend.attendances.entity.exception.AttendanceErrorCode;
import com.mos.backend.attendances.infrastructure.AttendanceRepository;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studymembers.entity.exception.StudyMemberErrorCode;
import com.mos.backend.studymembers.infrastructure.StudyMemberRepository;
import com.mos.backend.studyschedules.entity.StudySchedule;
import com.mos.backend.studyschedules.entity.exception.StudyScheduleErrorCode;
import com.mos.backend.studyschedules.infrastructure.StudyScheduleRepository;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mos.backend.attendances.application.res.StudyMemberAttendanceRes.AttendanceRes;

@RequiredArgsConstructor
@Service
public class AttendanceService {

    private final EntityFacade entityFacade;
    private final AttendanceRepository attendanceRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyScheduleRepository studyScheduleRepository;

    @Transactional
    public void create(Long userId, Long studyId, Long studyScheduleId) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);
        StudySchedule studySchedule = entityFacade.getStudySchedule(studyScheduleId);

        validateRelation(study, studySchedule);
        validateIsCompleted(studySchedule);

        StudyMember studyMember = studyMemberRepository.findByUserIdAndStudyId(user.getId(), study.getId())
                .orElseThrow(() -> new MosException(StudyMemberErrorCode.STUDY_MEMBER_NOT_FOUND));

        validateAlreadyExist(studySchedule, studyMember);

        if (studySchedule.isBeforePresentTime())
            throw new MosException(AttendanceErrorCode.NOT_PRESENT_TIME);

        Attendance attendance = studySchedule.isPresentTime()
                ? Attendance.createPresentAttendance(studySchedule, studyMember)
                : Attendance.createLateAttendance(studySchedule, studyMember);

        attendanceRepository.save(attendance);
    }

    @Transactional
    public void update(Long userId, Long studyId, Long studyScheduleId, String attendanceStatusDescription) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);
        StudySchedule studySchedule = entityFacade.getStudySchedule(studyScheduleId);

        validateRelation(study, studySchedule);
        //todo 팀장 권한 검증

        StudyMember studyMember = studyMemberRepository.findByUserIdAndStudyId(user.getId(), study.getId())
                .orElseThrow(() -> new MosException(StudyMemberErrorCode.STUDY_MEMBER_NOT_FOUND));

        AttendanceStatus attendanceStatus = AttendanceStatus.fromDescription(attendanceStatusDescription);

        attendanceRepository.findByStudyScheduleAndStudyMember(studySchedule, studyMember)
                .ifPresentOrElse(attendance -> attendance.updateStatus(attendanceStatus)
                        , () -> {
                            Attendance attendance = Attendance.create(studySchedule, studyMember, attendanceStatus);
                            attendanceRepository.save(attendance);
                        }
                );
    }

    @Transactional(readOnly = true)
    public List<StudyMemberAttendanceRes> getStudyMemberAttendances(Long userId, Long studyId) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);

        List<StudyMember> studyMembers = studyMemberRepository.findAllByStudyId(study.getId());

        List<StudyMemberAttendanceRes> studyMemberAttendanceResList = studyMembers.stream()
                .map(studyMember -> {
                    List<Attendance> attendances = attendanceRepository.findAllByStudyMemberId(studyMember.getId());
                    List<AttendanceRes> attendanceResList = convertToRes(attendances);
                    double attendanceRate = calculateAttendanceRate(attendances);
                    return StudyMemberAttendanceRes.of(studyMember, attendanceResList, attendanceRate);
                })
                .toList();

        return studyMemberAttendanceResList;
    }

    @Transactional(readOnly = true)
    public List<StudyScheduleAttendanceRateRes> getStudyScheduleAttendanceRates(Long userId, Long studyId) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);

        List<StudySchedule> studySchedules = studyScheduleRepository.findByStudyId(study.getId());
        List<StudyScheduleAttendanceRateRes> studyScheduleAttendanceRateResList = studySchedules.stream()
                .map(studySchedule -> {
                    List<Attendance> attendances = attendanceRepository.findAllByStudyScheduleId(studySchedule.getId());
                    double attendanceRate = calculateAttendanceRate(attendances);
                    return StudyScheduleAttendanceRateRes.of(studySchedule, attendanceRate);
                })
                .toList();

        return studyScheduleAttendanceRateResList;
    }

    @Transactional
    public void delete(Long userId, Long studyId, Long studyScheduleId, Long attendanceId) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);
        StudySchedule studySchedule = entityFacade.getStudySchedule(studyScheduleId);
        Attendance attendance = entityFacade.getAttendance(attendanceId);

        validateRelation(study, studySchedule);

        StudyMember studyMember = studyMemberRepository.findByUserIdAndStudyId(user.getId(), study.getId())
                .orElseThrow(() -> new MosException(StudyMemberErrorCode.STUDY_MEMBER_NOT_FOUND));

        validateRelatedAttendance(attendance, studySchedule, studyMember);

        attendanceRepository.delete(attendance);
    }

    private static void validateRelatedAttendance(Attendance attendance, StudySchedule studySchedule, StudyMember studyMember) {
        if (!attendance.isRelated(studySchedule.getId(), studyMember.getId()))
            throw new MosException(AttendanceErrorCode.UNRELATED_ATTENDANCE);
    }

    public static double calculateAttendanceRate(List<Attendance> attendances) {
        if (attendances.isEmpty()) {
            return 0.0;
        }
        double rate = (double) attendances.stream().filter(Attendance::isAttended).count() / attendances.size() * 100;
        return Math.round(rate * 10) / 10.0;
    }

    private static List<AttendanceRes> convertToRes(List<Attendance> attendances) {
        return attendances.stream()
                .map(attendance -> {
                    boolean isAttended = attendance.isAttended();
                    return AttendanceRes.of(attendance, isAttended);
                }).toList();
    }

    private static void validateRelation(Study study, StudySchedule studySchedule) {
        if (!study.isRelated(studySchedule.getStudy().getId()))
            throw new MosException(StudyErrorCode.UNRELATED_STUDY);
    }

    private static void validateIsCompleted(StudySchedule studySchedule) {
        if (studySchedule.isCompleted())
            throw new MosException(StudyScheduleErrorCode.STUDY_SCHEDULE_COMPLETED);
    }

    private void validateAlreadyExist(StudySchedule studySchedule, StudyMember studyMember) {
        attendanceRepository.findByStudyScheduleAndStudyMember(studySchedule, studyMember)
                .ifPresent(attendance -> {
                    throw new MosException(AttendanceErrorCode.ATTENDANCE_ALREADY_EXISTS);
                });
    }
}
