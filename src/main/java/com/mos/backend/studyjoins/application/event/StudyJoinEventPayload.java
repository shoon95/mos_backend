package com.mos.backend.studyjoins.application.event;

import com.mos.backend.common.event.Payload;
import com.mos.backend.hotstudies.entity.HotStudyEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyJoinEventPayload implements Payload {
    HotStudyEventType type;
    Long studyId;
}