package com.mos.backend.studychatmessages.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.common.redis.RedisPublisher;
import com.mos.backend.studychatmessages.application.dto.StudyChatMessageDto;
import com.mos.backend.studychatmessages.entity.StudyChatMessage;
import com.mos.backend.studychatmessages.infrastructure.StudyChatMessageRepository;
import com.mos.backend.studychatmessages.presentation.req.StudyChatMessagePublishReq;
import com.mos.backend.studychatrooms.entity.StudyChatRoom;
import com.mos.backend.studychatrooms.entity.StudyChatRoomErrorCode;
import com.mos.backend.studymembers.infrastructure.StudyMemberRepository;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StudyChatMessageService {
    private final StudyMemberRepository studyMemberRepository;
    private final StudyChatMessageRepository studyChatMessageRepository;
    private final RedisPublisher redisPublisher;
    private final EntityFacade entityFacade;

    @Transactional
    public void publish(Long userId, StudyChatMessagePublishReq req) {
        User user = entityFacade.getUser(userId);
        StudyChatRoom studyChatRoom = entityFacade.getStudyChatRoom(req.getStudyChatRoomId());

        if (!studyMemberRepository.existsByUserAndStudy(user, studyChatRoom.getStudy()))
            throw new MosException(StudyChatRoomErrorCode.FORBIDDEN);

        StudyChatMessage studyChatMessage = saveStudyChatMessage(req, user, studyChatRoom);

        StudyChatMessageDto studyChatMessageDto = StudyChatMessageDto.of(studyChatMessage, user.getId());
        redisPublisher.publishStudyChatMessage(studyChatMessageDto);
    }

    private StudyChatMessage saveStudyChatMessage(StudyChatMessagePublishReq req, User user, StudyChatRoom studyChatRoom) {
        StudyChatMessage studyChatMessage = StudyChatMessage.of(user, studyChatRoom, req.getMessage());
        return studyChatMessageRepository.save(studyChatMessage);
    }
}
