package com.mos.backend.privatechatrooms.presentation.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PrivateChatRoomCreateReq {
    private Long receiverId;
}
