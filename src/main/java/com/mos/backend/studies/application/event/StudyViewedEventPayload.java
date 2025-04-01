package com.mos.backend.studies.application.event;

import com.mos.backend.common.event.Payload;
import com.mos.backend.hotstudies.entity.HotStudyEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyViewedEventPayload implements Payload {
    HotStudyEventType type;
    Long studyId;
}