package com.mos.backend.studychatrooms.infrastructure;

import com.mos.backend.studychatrooms.entity.StudyChatRoom;

import java.util.Optional;

public interface StudyChatRoomRepository {
    Optional<StudyChatRoom> findById(Long id);
}
