package com.mos.backend.studychatmessages.infrastructure;

import com.mos.backend.studychatmessages.entity.StudyChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyChatMessageJpaRepository extends JpaRepository<StudyChatMessage, Long> {
}
