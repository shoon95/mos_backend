package com.mos.backend.studyquestions.entity;

import com.mos.backend.common.exception.MosException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum QuestionType {
    MULTIPLE_CHOICE("객관식"),
    SHORT_ANSWER("주관식");

    private final String description;

    public static QuestionType fromDescription(String description) {
        return Arrays.stream(QuestionType.values())
                .filter(d -> d.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new MosException(StudyQuestionErrorCode.INVALID_QUESTION_TYPE));
    }
}
