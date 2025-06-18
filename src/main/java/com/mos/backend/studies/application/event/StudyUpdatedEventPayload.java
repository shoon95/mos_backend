package com.mos.backend.studies.application.event;

import com.mos.backend.common.event.Payload;
import com.mos.backend.studies.presentation.requestdto.StudyUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyUpdatedEventPayload implements Payload {
    private Long userId;
    private StudyUpdateRequestDto requestDto;
    private Long studyId;
}
