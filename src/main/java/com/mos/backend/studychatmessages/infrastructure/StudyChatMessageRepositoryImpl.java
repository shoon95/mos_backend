package com.mos.backend.studychatmessages.infrastructure;

import com.mos.backend.studychatmessages.entity.StudyChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StudyChatMessageRepositoryImpl implements StudyChatMessageRepository {
    private final StudyChatMessageJpaRepository studyChatMessageJpaRepository;

    @Override
    public StudyChatMessage save(StudyChatMessage studyChatMessage) {
        return studyChatMessageJpaRepository.save(studyChatMessage);
    }
}
