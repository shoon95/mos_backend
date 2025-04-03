package com.mos.backend.privatechatrooms.presentation.controller.api;

import com.mos.backend.privatechatrooms.application.PrivateChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PrivateChatRoomController {
    private final PrivateChatRoomService privateChatRoomService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/private-chat-rooms/{privateChatRoomId}")
    public void enter(@AuthenticationPrincipal Long userId, @PathVariable Long privateChatRoomId) {
        privateChatRoomService.enter(userId, privateChatRoomId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/private-chat-rooms/{privateChatRoomId}")
    public void leave(@AuthenticationPrincipal Long userId, @PathVariable Long privateChatRoomId) {
        privateChatRoomService.leave(userId, privateChatRoomId);
    }

}
