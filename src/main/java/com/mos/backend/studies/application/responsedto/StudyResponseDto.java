package com.mos.backend.studies.application.responsedto;

import com.mos.backend.studies.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class StudyResponseDto {
    private Long id;
    private String title;
    private String subNotice;
    private String content;
    private Long currentStudyMemberCount;
    private Integer maxStudyMemberCount;
    private String category;
    private String schedule;
    private LocalDate recruitmentStartDate;
    private LocalDate recruitmentEndDate;
    private Integer viewCount;
    private String recruitmentStatus;
    private String progressStatus;
    private String meetingType;
    private List<String> tags;
    private Long likedCount;
    private Boolean isLiked;

    public StudyResponseDto(Long studyId, String title, String notice, String content, Long currentStudyMemberCount, Integer maxStudyMemberCount, Category category, String schedule,
                            LocalDate recruitmentStartDate, LocalDate recruitmentEndDate, Integer viewCount, RecruitmentStatus recruitmentStatus, ProgressStatus progressStatus, MeetingType meetingType,
                            StudyTag tags, Long likedCount, Boolean isLiked) {
        this.id = studyId;
        this.title = title;
        this.subNotice = notice;
        this.content = content;
        this.currentStudyMemberCount = currentStudyMemberCount;
        this.maxStudyMemberCount = maxStudyMemberCount;
        this.category = category.getDescription();
        this.schedule = schedule;
        this.recruitmentStartDate = recruitmentStartDate;
        this.recruitmentEndDate = recruitmentEndDate;
        this.viewCount = viewCount;
        this.recruitmentStatus = recruitmentStatus.getDescription();
        this.progressStatus = progressStatus.getDescription();
        this.meetingType = meetingType.getDescription();
        this.tags = tags.toList();
        this.likedCount = likedCount;
        this.isLiked = isLiked;
    }
}

