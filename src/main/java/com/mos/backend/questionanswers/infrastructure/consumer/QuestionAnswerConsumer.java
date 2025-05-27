package com.mos.backend.questionanswers.infrastructure.consumer;

import com.mos.backend.common.event.Event;
import com.mos.backend.questionanswers.application.QuestionAnswerService;
import com.mos.backend.studyjoins.application.event.StudyJoinCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuestionAnswerConsumer {

    private final QuestionAnswerService questionAnswerService;

    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void handleStudyCreatedEvent(Event<StudyJoinCreatedEventPayload> event) {
        StudyJoinCreatedEventPayload payload = event.getPayload();
        questionAnswerService.create(payload.getStudyJoinId(), payload.getStudyQuestionId(), payload.getAnswer());
    }
}