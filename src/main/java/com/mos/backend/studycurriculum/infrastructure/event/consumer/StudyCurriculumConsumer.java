package com.mos.backend.studycurriculum.infrastructure.event.consumer;

import com.mos.backend.common.event.Event;
import com.mos.backend.studies.application.event.StudyCreatedEventPayload;
import com.mos.backend.studycurriculum.application.StudyCurriculumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudyCurriculumConsumer {

    private final StudyCurriculumService studyCurriculumService;

    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void handleStudyCreatedEvent(Event<StudyCreatedEventPayload> event) {
        StudyCreatedEventPayload payload = event.getPayload();
        studyCurriculumService.createOrUpdateOrDelete(payload.getStudyId(), payload.getRequestDto().getCurriculums());
    }
}
