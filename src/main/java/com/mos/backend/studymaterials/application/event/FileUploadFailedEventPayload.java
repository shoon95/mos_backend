package com.mos.backend.studymaterials.application.event;

import com.mos.backend.common.event.Payload;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileUploadFailedEventPayload implements Payload {
    private final String filePath;
}
