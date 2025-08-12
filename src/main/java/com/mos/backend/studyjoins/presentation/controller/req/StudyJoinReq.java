package com.mos.backend.studyjoins.presentation.controller.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudyJoinReq {
    private Long studyQuestionId;
    private String answer;
}
