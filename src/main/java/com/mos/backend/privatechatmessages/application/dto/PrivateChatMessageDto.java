package com.mos.backend.privatechatmessages.application.dto;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateChatMessageDto implements Serializable {
    private Long userId;
    private Long privateChatRoomId;
    private String message;
    private LocalDateTime sendTime;

    public static PrivateChatMessageDto of(PrivateChatMessage privateChatMessage, Long userId) {
        PrivateChatMessageDto privateChatMessageDto = new PrivateChatMessageDto();
        privateChatMessageDto.userId = userId;
        privateChatMessageDto.privateChatRoomId = privateChatMessage.getPrivateChatRoom().getId();
        privateChatMessageDto.message = privateChatMessage.getMessage();
        privateChatMessageDto.sendTime = privateChatMessage.getCreatedAt();
        return privateChatMessageDto;
    }
}
