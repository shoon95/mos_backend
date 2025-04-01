package com.mos.backend.studies.application.event;

import com.mos.backend.common.event.Payload;
import com.mos.backend.studies.presentation.requestdto.StudyCreateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyCreatedEventPayload implements Payload {
    private Long userId;
    private StudyCreateRequestDto requestDto;
    private Long studyId;
}
