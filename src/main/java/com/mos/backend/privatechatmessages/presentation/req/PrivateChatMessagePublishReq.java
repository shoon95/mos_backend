package com.mos.backend.privatechatmessages.presentation.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PrivateChatMessagePublishReq {
    private Long privateChatRoomId;
    private String message;
}
