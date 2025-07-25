package com.mos.backend.studychatrooms.application.res;

import com.mos.backend.studychatmessages.entity.StudyChatMessage;
import com.mos.backend.studychatrooms.entity.StudyChatRoom;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MyStudyChatRoomRes {
    private Long studyChatRoomId;
    private String chatName;

    private String lastMessage;
    private LocalDateTime lastMessageAt;

    private int unreadCnt;

    public static MyStudyChatRoomRes of(StudyChatRoom studyChatRoom, StudyChatMessage studyChatMessage, int unreadCnt) {
        MyStudyChatRoomRes myStudyChatRoomRes = new MyStudyChatRoomRes();

        myStudyChatRoomRes.studyChatRoomId = studyChatRoom.getId();
        myStudyChatRoomRes.chatName = studyChatRoom.getName();

        myStudyChatRoomRes.lastMessage = studyChatMessage.getMessage();
        myStudyChatRoomRes.lastMessageAt = studyChatMessage.getCreatedAt();

        myStudyChatRoomRes.unreadCnt = unreadCnt;

        return myStudyChatRoomRes;
    }
}
