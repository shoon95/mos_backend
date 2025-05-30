package com.mos.backend.privatechatmessages.infrastructure;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PrivateChatMessageRepositoryImpl implements PrivateChatMessageRepository {
    private final PrivateChatMessageJpaRepository privateChatMessageJpaRepository;
    private final PrivateChatMessageQueryDslRepository privateChatMessageQueryDslRepository;

    @Override
    public PrivateChatMessage save(PrivateChatMessage privateChatMessage) {
        return privateChatMessageJpaRepository.save(privateChatMessage);
    }

    @Override
    public Optional<PrivateChatMessage> findFirstByPrivateChatRoomOrderByCreatedByDesc(PrivateChatRoom privateChatRoom) {
        return privateChatMessageJpaRepository.findFirstByPrivateChatRoomOrderByCreatedByDesc(privateChatRoom);
    }

    @Override
    public List<PrivateChatMessage> findAllByChatRoomIdForInfiniteScroll(Long privateChatRoomId, Long lastPrivateChatMessageId, int size) {
        return privateChatMessageQueryDslRepository.findByChatRoomIdForInfiniteScroll(privateChatRoomId, lastPrivateChatMessageId, size);
    }
}
