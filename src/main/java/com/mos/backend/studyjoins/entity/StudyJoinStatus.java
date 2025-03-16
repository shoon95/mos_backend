package com.mos.backend.studyjoins.entity;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studyjoins.entity.exception.StudyJoinErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum StudyJoinStatus {
    PENDING("대기"),
    REJECTED("탈락"),
    APPROVED("승낙"),
    CANCELED("취소");

    private final String description;

    public static StudyJoinStatus fromDescription(String status) {
        return Arrays.stream(StudyJoinStatus.values())
                .filter(sjs -> sjs.description.equals(status))
                .findFirst().orElseThrow(()-> new MosException(StudyJoinErrorCode.STUDY_JOIN_STATUS_NOT_FOUND));
    }
}
