package com.mos.backend.privatechatmessages.infrastructure;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateChatMessageJpaRepository extends JpaRepository<PrivateChatMessage, Long> {
}
