package com.mos.backend.privatechatmessages.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.common.redis.RedisPublisher;
import com.mos.backend.privatechatmessages.application.dto.PrivateChatMessageDto;
import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import com.mos.backend.privatechatmessages.infrastructure.PrivateChatMessageRepository;
import com.mos.backend.privatechatmessages.presentation.req.PrivateChatMessagePublishReq;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoomErrorCode;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PrivateChatMessageService {

    private final PrivateChatMessageRepository privateChatMessageRepository;
    private final EntityFacade entityFacade;
    private final RedisPublisher redisPublisher;

    @Transactional
    public void publish(Long userId, PrivateChatMessagePublishReq req) {
        User user = entityFacade.getUser(userId);
        PrivateChatRoom privateChatRoom = entityFacade.getPrivateChatRoom(req.getPrivateChatRoomId());

        if (isNotChatRoomMember(user, privateChatRoom))
            throw new MosException(PrivateChatRoomErrorCode.FORBIDDEN);

        privateChatRoom.visible();

        PrivateChatMessage privateChatMessage = savePrivateChatMessage(user, privateChatRoom, req.getMessage());

        redisPublisher.publishPrivateChatMessage(PrivateChatMessageDto.of(privateChatMessage, user.getId()));
    }

    private boolean isNotChatRoomMember(User user, PrivateChatRoom privateChatRoom) {
        Long userId = user.getId();
        return !userId.equals(privateChatRoom.getRequester().getId())
                && !userId.equals(privateChatRoom.getReceiver().getId());
    }

    private PrivateChatMessage savePrivateChatMessage(User user, PrivateChatRoom privateChatRoom, String message) {
        PrivateChatMessage privateChatMessage = PrivateChatMessage.of(user, privateChatRoom, message);
        return privateChatMessageRepository.save(privateChatMessage);
    }
}
