package com.mos.backend.privatechatmessages.application.dto;

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
    private String nickname;
    private Long privateChatRoomId;
    private String message;
    private LocalDateTime messageCreatedAt;

    public static PrivateChatMessageDto of(Long userId, String nickname, Long privateChatRoomId, String message, LocalDateTime messageCreatedAt) {
        PrivateChatMessageDto privateChatMessageDto = new PrivateChatMessageDto();
        privateChatMessageDto.userId = userId;
        privateChatMessageDto.nickname = nickname;
        privateChatMessageDto.privateChatRoomId = privateChatRoomId;
        privateChatMessageDto.message = message;
        privateChatMessageDto.messageCreatedAt = messageCreatedAt;
        return privateChatMessageDto;
    }
}
