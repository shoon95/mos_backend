package com.mos.backend.studyjoins.presentation.controller.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyJoinReq {
    private Long studyQuestionId;
    private String answer;
}
