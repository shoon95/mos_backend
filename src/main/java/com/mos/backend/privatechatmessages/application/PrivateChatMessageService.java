package com.mos.backend.privatechatmessages.application;

import com.mos.backend.common.dto.InfinityScrollRes;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.common.redis.RedisPublisher;
import com.mos.backend.common.utils.InfinityScrollUtil;
import com.mos.backend.privatechatmessages.application.dto.PrivateChatMessageDto;
import com.mos.backend.privatechatmessages.application.res.PrivateChatMessageRes;
import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import com.mos.backend.privatechatmessages.infrastructure.PrivateChatMessageRepository;
import com.mos.backend.privatechatmessages.presentation.req.PrivateChatMessagePublishReq;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoomErrorCode;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @PreAuthorize("@userSecurity.isOwnerOrAdmin(#userId)")
    @Transactional(readOnly = true)
    public InfinityScrollRes<PrivateChatMessageRes> getPrivateChatMessages(Long userId, Long privateChatRoomId, Long lastPrivateChatMessageId, int size) {
        User user = entityFacade.getUser(userId);
        PrivateChatRoom privateChatRoom = entityFacade.getPrivateChatRoom(privateChatRoomId);

        if (isNotChatRoomMember(user, privateChatRoom))
            throw new MosException(PrivateChatRoomErrorCode.FORBIDDEN);

        List<PrivateChatMessage> privateChatMessages = privateChatMessageRepository.findAllByChatRoomIdForInfiniteScroll(
                privateChatRoom.getId(), lastPrivateChatMessageId, size
        );

        boolean hasNext = InfinityScrollUtil.hasNext(privateChatMessages, size);
        if (hasNext)
            privateChatMessages.remove(privateChatMessages.size() - 1);

        Optional<PrivateChatMessage> lastElement = InfinityScrollUtil.getLastElement(privateChatMessages);
        Long lastElementId = lastElement.map(PrivateChatMessage::getId).orElse(null);

        List<PrivateChatMessageRes> privateChatMessageResList = privateChatMessages.stream()
                .map(PrivateChatMessageRes::of)
                .toList();

        return InfinityScrollRes.of(privateChatMessageResList, lastElementId, hasNext);
    }
}
