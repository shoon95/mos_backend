package com.mos.backend.studychatmessages.presentation.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyChatMessagePublishReq {
    private Long studyChatRoomId;
    private String message;
}
