package com.mos.backend.privatechatrooms.application.res;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MyPrivateChatRoomRes {
    private Long counterpartId;

    private Long privateChatRoomId;
    private String lastMessage;
    private LocalDateTime lastMessageAt;

    public static MyPrivateChatRoomRes of(PrivateChatRoom privateChatRoom, Long counterpartId, PrivateChatMessage privateChatMessage) {
        MyPrivateChatRoomRes myPrivateChatRoomRes = new MyPrivateChatRoomRes();
        myPrivateChatRoomRes.counterpartId = counterpartId;
        myPrivateChatRoomRes.privateChatRoomId = privateChatRoom.getId();
        myPrivateChatRoomRes.lastMessage = privateChatMessage.getMessage();
        myPrivateChatRoomRes.lastMessageAt = privateChatMessage.getCreatedAt();
        return myPrivateChatRoomRes;
    }

    public static MyPrivateChatRoomRes of(PrivateChatRoom privateChatRoom, Long counterpartId) {
        MyPrivateChatRoomRes myPrivateChatRoomRes = new MyPrivateChatRoomRes();
        myPrivateChatRoomRes.counterpartId = counterpartId;
        myPrivateChatRoomRes.privateChatRoomId = privateChatRoom.getId();
        myPrivateChatRoomRes.lastMessage = Strings.EMPTY;
        myPrivateChatRoomRes.lastMessageAt = null;
        return myPrivateChatRoomRes;
    }

}
