package com.mos.backend.userstudylikes.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikesResponseDto {
    private Long studyId;
    private Long likedCount;
    private Boolean isLiked;
}
