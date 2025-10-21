package com.mos.backend.attendances.presentation.controller.api;

import com.mos.backend.attendances.application.AttendanceService;
import com.mos.backend.attendances.application.res.StudyMemberAttendanceRes;
import com.mos.backend.attendances.application.res.StudyScheduleAttendanceRateRes;
import com.mos.backend.attendances.presentation.req.AttendanceUpdateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AttendanceController {
    private final AttendanceService attendanceService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/studies/{studyId}/schedules/{studyScheduleId}/attendances")
    public void create(@AuthenticationPrincipal Long userId,
                       @PathVariable Long studyId,
                       @PathVariable Long studyScheduleId) {
        attendanceService.create(userId, studyId, studyScheduleId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/studies/{studyId}/schedules/{studyScheduleId}/attendances")
    public void update(@AuthenticationPrincipal Long userId,
                       @PathVariable Long studyId,
                       @PathVariable Long studyScheduleId,
                       @RequestBody AttendanceUpdateReq attendanceUpdateReq) {
        attendanceService.update(userId, studyId, studyScheduleId, attendanceUpdateReq);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/studies/{studyId}/members/attendances")
    public List<StudyMemberAttendanceRes> getStudyMemberAttendances(@AuthenticationPrincipal Long userId, @PathVariable Long studyId) {
        return attendanceService.getStudyMemberAttendances(userId, studyId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/studies/{studyId}/schedules/attendances")
    public List<StudyScheduleAttendanceRateRes> getStudyScheduleAttendanceRates(@AuthenticationPrincipal Long userId, @PathVariable Long studyId) {
        return attendanceService.getStudyScheduleAttendanceRates(userId, studyId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/studies/{studyId}/schedules/{studyScheduleId}/attendances/{attendanceId}")
    public void delete(@AuthenticationPrincipal Long userId,
                       @PathVariable Long studyId,
                       @PathVariable Long studyScheduleId,
                       @PathVariable Long attendanceId) {
        attendanceService.delete(userId, studyId, studyScheduleId, attendanceId);
    }
}
