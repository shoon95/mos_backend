package com.mos.backend.privatechatmessages.presentation.controller.api;

import com.mos.backend.common.dto.InfinityScrollRes;
import com.mos.backend.common.utils.StompPrincipalUtil;
import com.mos.backend.privatechatmessages.application.PrivateChatMessageService;
import com.mos.backend.privatechatmessages.application.res.PrivateChatMessageRes;
import com.mos.backend.privatechatmessages.presentation.req.PrivateChatMessagePublishReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PrivateChatMessageController {
    private final PrivateChatMessageService privateChatMessageService;

    @MessageMapping("/privateChatRooms/{privateChatRoomId}/messages")
    public void publishPrivateChatMessage(Message<?> message,
                                          @DestinationVariable Long privateChatRoomId,
                                          @Validated PrivateChatMessagePublishReq privateChatMessagePublishReq) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Long userId = StompPrincipalUtil.getUserId(accessor);
        privateChatMessageService.publish(userId, privateChatRoomId, privateChatMessagePublishReq);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/private-chat-rooms/{privateChatRoomId}/messages")
    public InfinityScrollRes<PrivateChatMessageRes> getPrivateChatMessages(@AuthenticationPrincipal Long userId,
                                                                           @PathVariable Long privateChatRoomId,
                                                                           @RequestParam(required = false) Long lastPrivateChatMessageId,
                                                                           @RequestParam(defaultValue = "10") int size) {
        return privateChatMessageService.getPrivateChatMessages(userId, privateChatRoomId, lastPrivateChatMessageId, size);
    }
}
