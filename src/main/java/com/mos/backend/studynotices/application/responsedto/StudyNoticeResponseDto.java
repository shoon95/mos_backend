package com.mos.backend.studynotices.application.responsedto;

import com.mos.backend.studynotices.entity.StudyNotice;
import com.mos.backend.users.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class StudyNoticeResponseDto {
    private long studyNoticeId;
    private String title;
    private String content;
    private boolean pinned;
    private boolean important;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private long creatorId;
    private String creatorNickname;
    private Long modifierId;
    private String modifierNickname;
    private long studyId;

    public static StudyNoticeResponseDto of (StudyNotice studyNotice, User creator, User modifier) {
        StudyNoticeResponseDto studyNoticeResponseDto = new StudyNoticeResponseDto();
        studyNoticeResponseDto.studyNoticeId = studyNotice.getId();
        studyNoticeResponseDto.title = studyNotice.getTitle();
        studyNoticeResponseDto.content = studyNotice.getContent();
        studyNoticeResponseDto.pinned = studyNotice.isPinned();
        studyNoticeResponseDto.important = studyNotice.isImportant();
        studyNoticeResponseDto.createdAt = studyNotice.getCreatedAt();
        studyNoticeResponseDto.modifiedAt = studyNotice.getModifiedAt();
        studyNoticeResponseDto.creatorId = creator.getId();
        studyNoticeResponseDto.creatorNickname = creator.getNickname();
        studyNoticeResponseDto.modifierId = studyNotice.getUpdatedBy();
        if (modifier == null) {
            studyNoticeResponseDto.modifierNickname = "알 수 없음";
        } else {
            studyNoticeResponseDto.modifierNickname = modifier.getNickname();
        }
        studyNoticeResponseDto.studyId = studyNotice.getStudy().getId();
        return studyNoticeResponseDto;
    }
}
