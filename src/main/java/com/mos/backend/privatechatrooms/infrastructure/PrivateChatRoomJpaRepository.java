package com.mos.backend.privatechatrooms.infrastructure;

import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateChatRoomJpaRepository extends JpaRepository<PrivateChatRoom, Long> {
}
