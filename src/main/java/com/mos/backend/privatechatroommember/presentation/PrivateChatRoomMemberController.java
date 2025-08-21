package com.mos.backend.privatechatroommember.presentation;

import com.mos.backend.privatechatroommember.application.PrivateChatRoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PrivateChatRoomMemberController {
    private final PrivateChatRoomMemberService privateChatRoomMemberService;

    @DeleteMapping("/private-chat-rooms/{privateChatRoomId}/members")
    public void leavePrivateChatRoom(@AuthenticationPrincipal Long userId, @PathVariable Long privateChatRoomId) {
        privateChatRoomMemberService.leavePrivateChatRoom(userId, privateChatRoomId);
    }
}
