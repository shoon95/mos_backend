package com.mos.backend.studymaterials.application.fileuploader;

import com.mos.backend.common.event.Event;
import com.mos.backend.studymaterials.application.StudyMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileUploadConsumer {

    private final StudyMaterialService studyMaterialService;

    @EventListener
    public void fileUploadFailedEventHandle(Event<FileUploadFailedEventPayload> event) {
        FileUploadFailedEventPayload payload = event.getPayload();
        studyMaterialService.delete(payload.getFilePath());
    }
}
