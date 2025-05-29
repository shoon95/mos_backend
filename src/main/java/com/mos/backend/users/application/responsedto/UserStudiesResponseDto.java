package com.mos.backend.users.application.responsedto;

import com.mos.backend.studies.entity.*;
import com.mos.backend.studymembers.entity.ParticipationStatus;
import com.mos.backend.studymembers.entity.StudyMemberRoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class UserStudiesResponseDto {
    private Long id;
    private String title;
    private String category;
    private String meetingType;
    private String progressStatus;
    private String participationStatus;
    private Long currentStudyMembers;
    private Integer maxStudyMembers;
    private String schedule;
    private String studyMemberRole;
    private List<String> tags;

    public UserStudiesResponseDto(Long studyId, String title, Category category, MeetingType meetingType, ProgressStatus progressStatus,
                                  ParticipationStatus participationStatus, Long currentStudyMembers, Integer maxStudyMembers, String schedule, StudyTag tags, StudyMemberRoleType studyMemberRoleType) {
        this.id = studyId;
        this.title = title;
        this.category = category.getDescription();
        this.meetingType = meetingType.getDescription();
        this.progressStatus = progressStatus.getDescription();
        this.currentStudyMembers = currentStudyMembers;
        this.maxStudyMembers = maxStudyMembers;
        this.schedule = schedule;
        this.participationStatus = participationStatus.getDescription();
        this.studyMemberRole = studyMemberRoleType.getDescription();
        this.tags = tags.toList();
    }
}
