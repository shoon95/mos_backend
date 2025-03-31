package com.mos.backend.studyjoins.presentation.controller.api;

import com.mos.backend.studyjoins.application.StudyJoinService;
import com.mos.backend.studyjoins.application.res.MyStudyJoinRes;
import com.mos.backend.studyjoins.application.res.StudyJoinRes;
import com.mos.backend.studyjoins.presentation.controller.req.StudyJoinReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StudyJoinController {

    private final StudyJoinService studyJoinService;

    @PostMapping("/studies/{studyId}/study-joins")
    @ResponseStatus(HttpStatus.OK)
    public void joinStudy(@AuthenticationPrincipal Long userId,
                          @PathVariable Long studyId,
                          @RequestBody List<StudyJoinReq> studyJoinReqs) {
        studyJoinService.joinStudy(userId, studyId, studyJoinReqs);
    }

    @GetMapping("/study-joins")
    @ResponseStatus(HttpStatus.OK)
    public List<MyStudyJoinRes> getMyStudyJoins(@AuthenticationPrincipal Long userId, @RequestParam String studyJoinStatus) {
        return studyJoinService.getMyStudyJoins(userId, studyJoinStatus);
    }

    @GetMapping("/studies/{studyId}/study-joins")
    @ResponseStatus(HttpStatus.OK)
    public List<StudyJoinRes> getStudyJoins(@AuthenticationPrincipal Long userId, @PathVariable Long studyId) {
        return studyJoinService.getStudyJoins(userId, studyId);
    }

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

    @PatchMapping("/studies/{studyId}/study-joins/{studyJoinId}")
    @ResponseStatus(HttpStatus.OK)
    public void cancelStudyJoin(@AuthenticationPrincipal Long userId,
                                @PathVariable Long studyId,
                                @PathVariable Long studyJoinId) {
        studyJoinService.cancelStudyJoin(userId, studyId, studyJoinId);
    }
}
