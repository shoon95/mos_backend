package com.mos.backend.studychatmessages.infrastructure;

import com.mos.backend.studychatmessages.entity.StudyChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class StudyChatMessageRepositoryImpl implements StudyChatMessageRepository {
    private final StudyChatMessageJpaRepository studyChatMessageJpaRepository;
    private final StudyChatMessageQueryDslRepository studyChatMessageQueryDslRepository;

    @Override
    public StudyChatMessage save(StudyChatMessage studyChatMessage) {
        return studyChatMessageJpaRepository.save(studyChatMessage);
    }

    @Override
    public List<StudyChatMessage> findAllByChatRoomIdForInfiniteScroll(Long studyChatRoomId, Long lastStudyChatMessageId, Integer size) {
        return studyChatMessageQueryDslRepository.findAllByChatRoomIdForInfiniteScroll(studyChatRoomId, lastStudyChatMessageId, size);
    }
}
