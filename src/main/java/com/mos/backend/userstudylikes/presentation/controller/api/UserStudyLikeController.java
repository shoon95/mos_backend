package com.mos.backend.userstudylikes.presentation.controller.api;

import com.mos.backend.userstudylikes.application.UserStudyLikeService;
import com.mos.backend.userstudylikes.application.response.LikesResponseDto;
import com.mos.backend.userstudylikes.application.response.UserStudyLikeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserStudyLikeController {

    private final UserStudyLikeService userStudyLikeService;

    @PostMapping("/studies/{studyId}/likes")
    @ResponseStatus(HttpStatus.OK)
    public UserStudyLikeResponseDto likes(@PathVariable Long studyId, @AuthenticationPrincipal Long userId) {
        return userStudyLikeService.like(studyId, userId);
    }

    @DeleteMapping("/studies/{studyId}/likes")
    @ResponseStatus(HttpStatus.OK)
    public UserStudyLikeResponseDto unlikes(@PathVariable Long studyId, @AuthenticationPrincipal Long userId) {
        return userStudyLikeService.unlike(studyId, userId);
    }

    @GetMapping("/likes")
    @ResponseStatus(HttpStatus.OK)
    public List<LikesResponseDto> getLikes(@RequestParam List<Long> studyIds, @AuthenticationPrincipal Long userId) {
        return userStudyLikeService.getLikes(studyIds, userId);
    }
}
