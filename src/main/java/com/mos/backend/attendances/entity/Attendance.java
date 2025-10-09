package com.mos.backend.attendances.entity;

import com.mos.backend.attendances.entity.exception.AttendanceErrorCode;
import com.mos.backend.common.entity.BaseTimeEntity;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studyschedules.entity.StudySchedule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import java.time.LocalDateTime;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

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
    @OnDelete(action = CASCADE)
    private StudySchedule studySchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_member_id", nullable = false)
    @OnDelete(action = CASCADE)
    private StudyMember studyMember;

    @Column(nullable = false)
    private AttendanceStatus attendanceStatus;

    public static Attendance create(StudySchedule studySchedule, StudyMember studyMember, AttendanceStatus attendanceStatus) {
        Attendance attendance = new Attendance();
        attendance.studySchedule = studySchedule;
        attendance.studyMember = studyMember;
        attendance.attendanceStatus = attendanceStatus;
        return attendance;
    }

    public static Attendance createWithThreshold(StudySchedule studySchedule, StudyMember studyMember, Integer lateThresholdMinutes, Integer absenceThresholdMinutes) {
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(studySchedule.getStartDateTime().minusMinutes(lateThresholdMinutes))) {
            throw new MosException(AttendanceErrorCode.NOT_PRESENT_TIME);
        }
        if (now.isAfter(studySchedule.getEndDateTime().plusMinutes(absenceThresholdMinutes))) {
            return create(studySchedule, studyMember, AttendanceStatus.UNEXCUSED_ABSENCE);
        }
        if (now.isAfter(studySchedule.getStartDateTime().plusMinutes(lateThresholdMinutes))) {
            return create(studySchedule, studyMember, AttendanceStatus.LATE);
        }
        return create(studySchedule, studyMember, AttendanceStatus.PRESENT);
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
