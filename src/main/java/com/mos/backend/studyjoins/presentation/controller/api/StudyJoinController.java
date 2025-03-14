package com.mos.backend.studyjoins.presentation.controller.api;

import com.mos.backend.studyjoins.application.StudyJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StudyJoinController {

    private final StudyJoinService studyJoinService;

    @PutMapping("/studies/{studyId}/study-joins/{studyJoinId}/approval")
    public ResponseEntity<Void> approveStudyJoin(@AuthenticationPrincipal Long userId,
                                                 @PathVariable Long studyId,
                                                 @PathVariable Long studyJoinId) {
        studyJoinService.approveStudyJoin(userId, studyId, studyJoinId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/studies/{studyId}/study-joins/{studyJoinId}/rejection")
    public ResponseEntity<Void> rejectStudyJoin(@AuthenticationPrincipal Long userId,
                                                @PathVariable Long studyId,
                                                @PathVariable Long studyJoinId) {
        studyJoinService.rejectStudyJoin(userId, studyId, studyJoinId);
        return ResponseEntity.ok().build();
    }
}
