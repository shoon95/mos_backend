package com.mos.backend.studynotices.application.event;

import com.mos.backend.common.event.Payload;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImportantNoticeChangedEventPayload implements Payload {
    private Long studyId;
}
