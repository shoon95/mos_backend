package com.mos.backend.studyrules.infrastructure.event.consumer;

import com.mos.backend.common.event.Event;
import com.mos.backend.studies.application.StudyCreatedEventPayload;
import com.mos.backend.studyrules.application.StudyRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudyRuleConsumer {

    private final StudyRuleService studyRuleService;

    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void handleStudyCreatedEvent(Event<StudyCreatedEventPayload> event) {
        StudyCreatedEventPayload payload = event.getPayload();
        studyRuleService.createOrUpdateOrDelete(payload.getStudyId(), payload.getRequestDto().getRules());
    }
}
