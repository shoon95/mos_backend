package com.mos.backend.privatechatmessages.infrastructure;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PrivateChatMessageRepositoryImpl implements PrivateChatMessageRepository {
    private final PrivateChatMessageJpaRepository privateChatMessageJpaRepository;

    @Override
    public PrivateChatMessage save(PrivateChatMessage privateChatMessage) {
        return privateChatMessageJpaRepository.save(privateChatMessage);
    }
}
