package com.mos.backend.privatechatrooms.infrastructure;

import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;

import java.util.Optional;

public interface PrivateChatRoomRepository {
    Optional<PrivateChatRoom> findById(Long id);
}
