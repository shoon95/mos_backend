package com.mos.backend.studychatrooms.infrastructure;

import com.mos.backend.studychatrooms.entity.StudyChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyChatRoomJpaRepository extends JpaRepository<StudyChatRoom, Long> {
}
