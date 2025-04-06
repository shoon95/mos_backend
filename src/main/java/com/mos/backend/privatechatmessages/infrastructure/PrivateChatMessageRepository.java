package com.mos.backend.privatechatmessages.infrastructure;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;

import java.util.Optional;

public interface PrivateChatMessageRepository {
    PrivateChatMessage save(PrivateChatMessage privateChatMessage);

    Optional<PrivateChatMessage> findFirstByPrivateChatRoomOrderByCreatedByDesc(PrivateChatRoom privateChatRoom);
}
