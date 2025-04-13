package com.mos.backend.privatechatrooms.application.res;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PrivateChatRoomRes {
    private Long counterpartId;

    private Long privateChatRoomId;
    private String lastMessage;
    private LocalDateTime lastMessageAt;

    public static PrivateChatRoomRes of(PrivateChatRoom privateChatRoom, Long counterpartId, PrivateChatMessage privateChatMessage) {
        PrivateChatRoomRes privateChatRoomRes = new PrivateChatRoomRes();
        privateChatRoomRes.counterpartId = counterpartId;
        privateChatRoomRes.privateChatRoomId = privateChatRoom.getId();
        privateChatRoomRes.lastMessage = privateChatMessage.getMessage();
        privateChatRoomRes.lastMessageAt = privateChatMessage.getCreatedAt();
        return privateChatRoomRes;
    }

    public static PrivateChatRoomRes of(PrivateChatRoom privateChatRoom, Long counterpartId) {
        PrivateChatRoomRes privateChatRoomRes = new PrivateChatRoomRes();
        privateChatRoomRes.counterpartId = counterpartId;
        privateChatRoomRes.privateChatRoomId = privateChatRoom.getId();
        privateChatRoomRes.lastMessage = Strings.EMPTY;
        privateChatRoomRes.lastMessageAt = null;
        return privateChatRoomRes;
    }
}
