package com.mos.backend.studychatmessages.presentation.controller.api;

import com.mos.backend.common.annotation.Sender;
import com.mos.backend.studychatmessages.application.StudyChatMessageService;
import com.mos.backend.studychatmessages.presentation.req.StudyChatMessagePublishReq;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StudyChatMessageController {
    private final StudyChatMessageService studyChatMessageService;

    @MessageMapping("/study-chat-messages")
    public void publishPrivateChatMessage(@Sender Long userId, StudyChatMessagePublishReq studyChatMessagePublishReq) {
        studyChatMessageService.publish(userId, studyChatMessagePublishReq);
    }
}
