package com.mos.backend.studies.application.event;

import com.mos.backend.common.event.Payload;
import com.mos.backend.hotstudies.entity.HotStudyEventType;
import com.mos.backend.studies.presentation.requestdto.StudyCreateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StudyDeletedEventPayload implements Payload {
    HotStudyEventType type;
    private Long userId;
    private StudyCreateRequestDto requestDto;
    private Long studyId;
}