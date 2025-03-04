package com.mos.backend.studies.entity;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Category {
    PROGRAMMING("프로그래밍"), BOOK("독서");

    private final String description;

    public static Category fromDescription(String description) {
        return Arrays.stream(Category.values())
                .filter(c -> c.description.equals(description))
                .findFirst().orElseThrow(()-> new MosException(StudyErrorCode.USER_NOT_FOUND_EXCEPTION));
    }
}
