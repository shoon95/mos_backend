package com.mos.backend.privatechatrooms.infrastructure;

import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PrivateChatRoomRepositoryImpl implements PrivateChatRoomRepository {
    private final PrivateChatRoomJpaRepository privateChatRoomJpaRepository;

    @Override
    public Optional<PrivateChatRoom> findById(Long id) {
        return privateChatRoomJpaRepository.findById(id);
    }
}
