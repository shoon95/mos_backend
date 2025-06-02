package com.mos.backend.studymaterials.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UploadType {
    USER("user"),
    STUDY("study"),
    TEMP("temp"),
    ;

    private final String folderPath;
}
