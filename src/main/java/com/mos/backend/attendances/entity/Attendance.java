package com.mos.backend.attendances.entity;

import com.mos.backend.common.entity.BaseTimeEntity;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studyschedules.entity.StudySchedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "attendances")
public class Attendance extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private StudySchedule studySchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_member_id", nullable = false)
    private StudyMember studyMember;

    @Column(nullable = false)
    private AttendanceStatus attendanceStatus;

    public static Attendance createPresentAttendance(StudySchedule studySchedule, StudyMember studyMember) {
        Attendance attendance = new Attendance();
        attendance.studySchedule = studySchedule;
        attendance.studyMember = studyMember;
        attendance.attendanceStatus = AttendanceStatus.PRESENT;
        return attendance;
    }

    public static Attendance createLateAttendance(StudySchedule studySchedule, StudyMember studyMember) {
        Attendance attendance = new Attendance();
        attendance.studySchedule = studySchedule;
        attendance.studyMember = studyMember;
        attendance.attendanceStatus = AttendanceStatus.LATE;
        return attendance;
    }

    public static Attendance create(StudySchedule studySchedule, StudyMember studyMember, AttendanceStatus attendanceStatus) {
        Attendance attendance = new Attendance();
        attendance.studySchedule = studySchedule;
        attendance.studyMember = studyMember;
        attendance.attendanceStatus = attendanceStatus;
        return attendance;
    }

    public void leaveEarly() {
        this.attendanceStatus = AttendanceStatus.EARLY_LEAVE;
    }

    public void updateStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public boolean isRelated(Long studyScheduleId, Long studyMemberId) {
        return this.studySchedule.getId().equals(studyScheduleId) && this.studyMember.getId().equals(studyMemberId);
    }

    public boolean isAttended() {
        return this.attendanceStatus == AttendanceStatus.PRESENT;
    }
}
