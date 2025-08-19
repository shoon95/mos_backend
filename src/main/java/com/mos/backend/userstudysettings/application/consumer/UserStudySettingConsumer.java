package com.mos.backend.userstudysettings.application.consumer;

import com.mos.backend.common.event.Event;
import com.mos.backend.studies.application.event.StudyCreatedEventPayload;
import com.mos.backend.studycurriculum.application.StudyCurriculumService;
import com.mos.backend.studynotices.application.event.ImportantNoticeChangedEventPayload;
import com.mos.backend.userstudysettings.application.UserStudySettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Component
@RequiredArgsConstructor
public class UserStudySettingConsumer {
    private final UserStudySettingService userStudySettingService;

    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void handleImportantNoticeChangedEvent(Event<ImportantNoticeChangedEventPayload> event) {
        ImportantNoticeChangedEventPayload payload = event.getPayload();
        userStudySettingService.showNoticeForAllMembers(payload.getStudyId());
    }
}
