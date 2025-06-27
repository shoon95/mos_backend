package com.mos.backend.studyschedules.presentation.controller.api;

import com.mos.backend.studyschedules.application.StudyScheduleService;
import com.mos.backend.studyschedules.application.res.StudyScheduleRes;
import com.mos.backend.studyschedules.presentation.req.StudyScheduleCreateReq;
import com.mos.backend.studyschedules.presentation.req.StudyScheduleUpdateReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StudyScheduleController {
    private final StudyScheduleService studyScheduleService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/studies/{studyId}/schedules")
    public void createStudySchedule(@PathVariable Long studyId,
                                    @Valid @RequestBody StudyScheduleCreateReq req) {
        studyScheduleService.createStudySchedule(studyId, req);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/study-schedules")
    public List<StudyScheduleRes> getMyStudySchedules(@AuthenticationPrincipal Long userId) {
        return studyScheduleService.getMyStudySchedules(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/studies/{studyId}/schedules")
    public List<StudyScheduleRes> getStudySchedules(@PathVariable Long studyId) {
        return studyScheduleService.getStudySchedules(studyId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/studies/{studyId}/study-schedules/{studyScheduleId}")
    public void updateStudySchedule(@PathVariable Long studyId,
                                    @PathVariable Long studyScheduleId,
                                    @Valid @RequestBody StudyScheduleUpdateReq req) {
        studyScheduleService.updateStudySchedule(studyId, studyScheduleId, req);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/studies/{studyId}/study-schedules/{studyScheduleId}")
    public void deleteStudySchedule(@PathVariable Long studyId,
                                    @PathVariable Long studyScheduleId) {
        studyScheduleService.deleteStudySchedule(studyId, studyScheduleId);
    }
}
