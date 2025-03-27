package com.mos.backend.studymembers.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudyMemberRoleType {
    LEADER("스터디장"), MEMBER("스터디원");

    private final String description;
}
