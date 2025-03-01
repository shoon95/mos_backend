package com.mos.backend.studyquestions.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionType {
    MULTIPLE_CHOICE("객관식"),
    SHORT_ANSWER("주관식");

    private final String description;
}
