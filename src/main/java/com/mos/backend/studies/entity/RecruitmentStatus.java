package com.mos.backend.studies.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecruitmentStatus {
    OPEN("모집 중"),
    CLOSED("모집 완료");

    private final String description;
}
