package com.mos.backend.studyjoins.application.event;

import com.mos.backend.common.event.Payload;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyJoinCreatedEventPayload implements Payload {
    private Long studyJoinId;
    private Long studyQuestionId;
    private String answer;
}
