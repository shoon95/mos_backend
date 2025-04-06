package com.mos.backend.privatechatmessages.infrastructure;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PrivateChatMessageRepositoryImpl implements PrivateChatMessageRepository {
    private final PrivateChatMessageJpaRepository privateChatMessageJpaRepository;

    @Override
    public PrivateChatMessage save(PrivateChatMessage privateChatMessage) {
        return privateChatMessageJpaRepository.save(privateChatMessage);
    }

    @Override
    public Optional<PrivateChatMessage> findFirstByPrivateChatRoomOrderByCreatedByDesc(PrivateChatRoom privateChatRoom) {
        return privateChatMessageJpaRepository.findFirstByPrivateChatRoomOrderByCreatedByDesc(privateChatRoom);
    }
}
