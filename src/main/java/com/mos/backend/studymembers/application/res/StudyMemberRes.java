package com.mos.backend.studymembers.application.res;

import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.users.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class StudyMemberRes {
    private Long userId;
    private String nickname;

    private String studyMemberRoleType;
    private LocalDate lastAttendanceDate;

    private double participationRate;

    public static StudyMemberRes of(StudyMember studyMember, LocalDate lastAttendanceDate, double participationRate) {
        User user = studyMember.getUser();

        StudyMemberRes studyMemberRes = new StudyMemberRes();
        studyMemberRes.userId = user.getId();
        studyMemberRes.nickname = user.getNickname();
        studyMemberRes.studyMemberRoleType = studyMember.getRoleType().getDescription();
        studyMemberRes.lastAttendanceDate = lastAttendanceDate;
        studyMemberRes.participationRate = participationRate;

        return studyMemberRes;
    }
}
