package com.mos.backend.privatechatrooms.application.res;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MyPrivateChatRoomRes {
    private Long privateChatRoomId;
    private String chatName;

    private String lastMessage;
    private LocalDateTime lastMessageAt;

    private int unreadCnt;

    public static MyPrivateChatRoomRes of(PrivateChatRoom privateChatRoom, PrivateChatMessage privateChatMessage, int unreadCnt) {
        MyPrivateChatRoomRes myPrivateChatRoomRes = new MyPrivateChatRoomRes();

        myPrivateChatRoomRes.privateChatRoomId = privateChatRoom.getId();
        myPrivateChatRoomRes.chatName = privateChatRoom.getName();

        myPrivateChatRoomRes.lastMessage = privateChatMessage.getMessage();
        myPrivateChatRoomRes.lastMessageAt = privateChatMessage.getCreatedAt();

        myPrivateChatRoomRes.unreadCnt = unreadCnt;
        return myPrivateChatRoomRes;
    }
}
