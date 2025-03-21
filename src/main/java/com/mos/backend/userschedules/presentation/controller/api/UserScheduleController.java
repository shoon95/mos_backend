package com.mos.backend.userschedules.presentation.controller.api;

import com.mos.backend.userschedules.application.UserScheduleService;
import com.mos.backend.userschedules.application.res.UserScheduleRes;
import com.mos.backend.userschedules.presentation.req.UserScheduleCreateReq;
import com.mos.backend.userschedules.presentation.req.UserScheduleUpdateReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserScheduleController {
    private final UserScheduleService userScheduleService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/user-schedules")
    public void createUserSchedule(@AuthenticationPrincipal Long userId, @Valid @RequestBody UserScheduleCreateReq req) {
        userScheduleService.createUserSchedule(userId, req);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user-schedules")
    public List<UserScheduleRes> getUserSchedules(@AuthenticationPrincipal Long userId) {
        return userScheduleService.getUserSchedules(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/user-schedules/{userScheduleId}")
    public void updateUserSchedule(@AuthenticationPrincipal Long userId,
                                   @PathVariable Long userScheduleId,
                                   @Valid @RequestBody UserScheduleUpdateReq req) {
        userScheduleService.updateUserSchedule(userId, userScheduleId, req);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/user-schedules/{userScheduleId}")
    public void deleteUserSchedule(@AuthenticationPrincipal Long userId, @PathVariable Long userScheduleId) {
        userScheduleService.deleteUserSchedule(userId, userScheduleId);
    }
}
