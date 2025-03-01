package com.mos.backend.studies.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    PROGRAMMING("프로그래밍"), BOOK("독서");

    private final String description;
}
