package com.mos.backend.studychatrooms.presentation.controller.api;

import com.mos.backend.studychatrooms.application.StudyChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class StudyChatRoomController {
    private final StudyChatRoomService studyChatRoomService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/study-chat-rooms/{studyChatRoomId}")
    public void enter(@AuthenticationPrincipal Long userId, @PathVariable Long studyChatRoomId) {
        studyChatRoomService.enter(userId, studyChatRoomId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/study-chat-rooms/{studyChatRoomId}")
    public void leave(@AuthenticationPrincipal Long userId, @PathVariable Long studyChatRoomId) {
        studyChatRoomService.leave(userId, studyChatRoomId);
    }

}
