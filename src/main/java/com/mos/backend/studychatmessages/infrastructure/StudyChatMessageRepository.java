package com.mos.backend.studychatmessages.infrastructure;

import com.mos.backend.studychatmessages.entity.StudyChatMessage;

import java.util.List;

public interface StudyChatMessageRepository {
    StudyChatMessage save(StudyChatMessage studyChatMessage);

    List<StudyChatMessage> findAllByChatRoomIdForInfiniteScroll(Long studyChatRoomId, Long lastStudyChatMessageId, Integer size);
}
