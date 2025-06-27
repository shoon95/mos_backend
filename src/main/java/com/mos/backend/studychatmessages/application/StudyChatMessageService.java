package com.mos.backend.studychatmessages.application;

import com.mos.backend.common.dto.InfinityScrollRes;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.common.redis.RedisPublisher;
import com.mos.backend.common.utils.InfinityScrollUtil;
import com.mos.backend.studychatmessages.application.dto.StudyChatMessageDto;
import com.mos.backend.studychatmessages.application.res.StudyChatMessageRes;
import com.mos.backend.studychatmessages.entity.StudyChatMessage;
import com.mos.backend.studychatmessages.infrastructure.StudyChatMessageRepository;
import com.mos.backend.studychatmessages.presentation.req.StudyChatMessagePublishReq;
import com.mos.backend.studychatrooms.entity.StudyChatRoom;
import com.mos.backend.studychatrooms.entity.StudyChatRoomErrorCode;
import com.mos.backend.studymembers.infrastructure.StudyMemberRepository;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @PreAuthorize("@studySecurity.isMemberOrAdmin(#studyId)")
    @Transactional(readOnly = true)
    public InfinityScrollRes<StudyChatMessageRes> getStudyChatMessages(Long studyId, Long studyChatRoomId, Long lastStudyChatMessageId, Integer size) {
        StudyChatRoom studyChatRoom = entityFacade.getStudyChatRoom(studyChatRoomId);

        List<StudyChatMessage> studyChatMessages = studyChatMessageRepository.findAllByChatRoomIdForInfiniteScroll(
                studyChatRoom.getId(), lastStudyChatMessageId, size
        );

        boolean hasNext = InfinityScrollUtil.hasNext(studyChatMessages, size);
        if (hasNext)
            studyChatMessages.remove(studyChatMessages.size() - 1);

        Optional<StudyChatMessage> lastElement = InfinityScrollUtil.getLastElement(studyChatMessages);
        Long lastElementId = lastElement.map(StudyChatMessage::getId).orElse(null);

        List<StudyChatMessageRes> studyChatMessageResList = studyChatMessages.stream()
                .map(StudyChatMessageRes::of)
                .toList();

        return InfinityScrollRes.of(studyChatMessageResList, lastElementId, hasNext);
    }
}
