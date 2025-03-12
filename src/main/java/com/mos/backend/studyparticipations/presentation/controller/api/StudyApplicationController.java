package com.mos.backend.studyparticipations.presentation.controller.api;

import com.mos.backend.studyparticipations.application.StudyApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/study-applications")
@RestController
public class StudyApplicationController {

    private final StudyApplicationService studyApplicationService;

    @PutMapping("/{studyApplicationId}/approval")
    public ResponseEntity<Void> approveStudyMember(@AuthenticationPrincipal Long userId, @PathVariable Long studyApplicationId) {
        studyApplicationService.approveStudyMember(userId, studyApplicationId);
        return ResponseEntity.ok().build();
    }
}
