package com.mos.backend.attendances.application.res;

import com.mos.backend.attendances.entity.Attendance;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studyschedules.entity.StudySchedule;
import com.mos.backend.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class StudyMemberAttendanceRes {
    private Long studyMemberId;

    private Long userId;
    private String nickname;

    private List<AttendanceRes> attendanceRes;

    private double attendanceRate;

    public static StudyMemberAttendanceRes of(StudyMember studyMember, List<AttendanceRes> attendanceResList, double attendanceRate) {
        User user = studyMember.getUser();
        return new StudyMemberAttendanceRes(studyMember.getId(), user.getId(), user.getNickname(), attendanceResList, attendanceRate);
    }


    @Getter
    @AllArgsConstructor
    public static class AttendanceRes {
        private Long attendanceId;
        private boolean isAttended;

        private Long studyScheduleId;
        private LocalDateTime StudyScheduleStartDateTime;

        public static AttendanceRes of(Attendance attendance, boolean isAttended) {
            StudySchedule studySchedule = attendance.getStudySchedule();
            return new AttendanceRes(attendance.getId(), isAttended, studySchedule.getId(), studySchedule.getStartDateTime());
        }
    }
}
