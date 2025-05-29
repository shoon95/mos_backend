package com.mos.backend.studymembers.entity;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studymembers.entity.exception.StudyMemberErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum StudyMemberRoleType {
    LEADER("스터디장"), MEMBER("스터디원");

    private final String description;

    public static StudyMemberRoleType fromDescription(String description) {
        return Arrays.stream(StudyMemberRoleType.values())
                .filter(r -> r.description.equals(description))
                .findFirst().orElseThrow(()-> new MosException(StudyMemberErrorCode.INVALID_STUDY_MEMBER_ROLE_TYPE));
    }
}
