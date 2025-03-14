package com.mos.backend.userstudylikes.presentation.controller.api;

import com.mos.backend.userstudylikes.application.UserStudyLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserStudyLikeRepository {

    private final UserStudyLikeService userStudyLikeService;

    @PostMapping("/studies/{studyId}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likes(@PathVariable Long studyId, @AuthenticationPrincipal Long userId) {
        userStudyLikeService.like(studyId, userId);
    }

    @DeleteMapping("/studies/{studyId}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikes(@PathVariable Long studyId, @AuthenticationPrincipal Long userId) {
        userStudyLikeService.unlike(studyId, userId);
    }
}
