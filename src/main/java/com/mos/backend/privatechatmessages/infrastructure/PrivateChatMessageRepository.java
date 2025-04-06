package com.mos.backend.privatechatmessages.infrastructure;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;

public interface PrivateChatMessageRepository {
    PrivateChatMessage save(PrivateChatMessage privateChatMessage);
}
