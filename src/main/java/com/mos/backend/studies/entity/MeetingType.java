package com.mos.backend.studies.entity;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MeetingType {
    OFFLINE("대면"),
    ONLINE("비대면"),
    HYBRID("혼합");

    private final String description;

    public static MeetingType fromDescription(String description) {
        return Arrays.stream(MeetingType.values())
                .filter(m -> m.description.equals(description))
                .findFirst().orElseThrow(()-> new MosException(StudyErrorCode.USER_NOT_FOUND_EXCEPTION));
    }
}
