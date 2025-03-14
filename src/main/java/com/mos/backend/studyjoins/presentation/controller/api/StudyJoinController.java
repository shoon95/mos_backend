package com.mos.backend.studyjoins.presentation.controller.api;

import com.mos.backend.studyjoins.application.StudyJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StudyJoinController {

    private final StudyJoinService studyJoinService;

    @PatchMapping("/studies/{studyId}/study-joins/{studyJoinId}/approval")
    @ResponseStatus(HttpStatus.OK)
    public void approveStudyJoin(@AuthenticationPrincipal Long userId,
                                 @PathVariable Long studyId,
                                 @PathVariable Long studyJoinId) {
        studyJoinService.approveStudyJoin(userId, studyId, studyJoinId);
    }

    @PatchMapping("/studies/{studyId}/study-joins/{studyJoinId}/rejection")
    @ResponseStatus(HttpStatus.OK)
    public void rejectStudyJoin(@AuthenticationPrincipal Long userId,
                                @PathVariable Long studyId,
                                @PathVariable Long studyJoinId) {
        studyJoinService.rejectStudyJoin(userId, studyId, studyJoinId);
    }
}
