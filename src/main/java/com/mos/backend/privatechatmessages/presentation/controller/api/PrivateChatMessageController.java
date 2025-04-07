package com.mos.backend.privatechatmessages.presentation.controller.api;

import com.mos.backend.common.annotation.Sender;
import com.mos.backend.privatechatmessages.application.PrivateChatMessageService;
import com.mos.backend.privatechatmessages.presentation.req.PrivateChatMessagePublishReq;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PrivateChatMessageController {
    private final PrivateChatMessageService privateChatMessageService;

    @MessageMapping("/private-chat-messages")
    public void publishPrivateChatMessage(@Sender Long userId, PrivateChatMessagePublishReq privateChatMessagePublishReq) {
        privateChatMessageService.publish(userId, privateChatMessagePublishReq);
    }
}
