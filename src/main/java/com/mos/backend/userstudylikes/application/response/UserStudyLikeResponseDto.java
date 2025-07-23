package com.mos.backend.userstudylikes.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserStudyLikeResponseDto {
    Long userId;
    Long studyId;
    Long likedCount;
}