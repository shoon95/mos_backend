package com.mos.backend.studychatmessages.infrastructure;

import com.mos.backend.studychatmessages.entity.StudyChatMessage;

public interface StudyChatMessageRepository {
    StudyChatMessage save(StudyChatMessage studyChatMessage);
}
