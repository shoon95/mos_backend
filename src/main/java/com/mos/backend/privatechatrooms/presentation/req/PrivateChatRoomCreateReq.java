package com.mos.backend.privatechatrooms.presentation.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PrivateChatRoomCreateReq {
    private Long receiverId;
}
