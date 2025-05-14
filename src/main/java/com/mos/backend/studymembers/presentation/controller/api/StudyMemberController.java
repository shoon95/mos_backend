package com.mos.backend.studymembers.presentation.controller.api;

import com.mos.backend.studymembers.application.StudyMemberService;
import com.mos.backend.studymembers.application.res.StudyMemberRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StudyMemberController {
    private final StudyMemberService studyMemberService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/studies/{studyId}/members")
    public List<StudyMemberRes> getStudyMembers(@PathVariable Long studyId) {
        return studyMemberService.getStudyMembers(studyId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/studies/{studyId}/members/{studyMemberId}")
    public void delegateLeader(@AuthenticationPrincipal Long userId,
                               @PathVariable Long studyId,
                               @PathVariable Long studyMemberId) {
        studyMemberService.delegateLeader(userId, studyId, studyMemberId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/studies/{studyId}/members")
    public void withDraw(@AuthenticationPrincipal Long userId, @PathVariable Long studyId) {
        studyMemberService.withDraw(userId, studyId);
    }
}
