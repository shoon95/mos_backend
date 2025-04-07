package com.mos.backend.privatechatmessages.infrastructure;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivateChatMessageJpaRepository extends JpaRepository<PrivateChatMessage, Long> {
    Optional<PrivateChatMessage> findFirstByPrivateChatRoomOrderByCreatedByDesc(PrivateChatRoom privateChatRoom);
}
