package com.mos.backend.privatechatmessages.application.res;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PrivateChatMessageRes {
    private Long privateChatMessageId;
    private String message;
    private LocalDateTime messageCreatedAt;

    private Long userId;
    private String nickname;

    public static PrivateChatMessageRes of(PrivateChatMessage privateChatMessage) {
        PrivateChatMessageRes privateChatMessageRes = new PrivateChatMessageRes();
        privateChatMessageRes.privateChatMessageId = privateChatMessage.getId();
        privateChatMessageRes.message = privateChatMessage.getMessage();
        privateChatMessageRes.messageCreatedAt = privateChatMessage.getCreatedAt();
        privateChatMessageRes.userId = privateChatMessage.getUser().getId();
        privateChatMessageRes.nickname = privateChatMessage.getUser().getNickname();
        return privateChatMessageRes;
    }
}
