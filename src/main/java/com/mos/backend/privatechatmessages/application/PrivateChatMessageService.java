package com.mos.backend.privatechatmessages.application;

import com.mos.backend.common.dto.InfinityScrollRes;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.common.redis.RedisPublisher;
import com.mos.backend.common.utils.InfinityScrollUtil;
import com.mos.backend.common.utils.StompSessionUtil;
import com.mos.backend.privatechatmessages.application.dto.PrivateChatMessageDto;
import com.mos.backend.privatechatmessages.application.res.PrivateChatMessageRes;
import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import com.mos.backend.privatechatmessages.infrastructure.PrivateChatMessageRepository;
import com.mos.backend.privatechatmessages.presentation.req.PrivateChatMessagePublishReq;
import com.mos.backend.privatechatroommember.application.PrivateChatRoomMemberService;
import com.mos.backend.privatechatroommember.entity.PrivateChatRoomMember;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PrivateChatMessageService {

    private final PrivateChatMessageRepository privateChatMessageRepository;
    private final EntityFacade entityFacade;
    private final RedisPublisher redisPublisher;
    private final PrivateChatRoomMemberService privateChatRoomMemberService;

    @Transactional
    public void publish(StompHeaderAccessor accessor, PrivateChatMessagePublishReq req) {
        Long userId = StompSessionUtil.getUserId(accessor);
        User user = entityFacade.getUser(userId);
        PrivateChatRoom privateChatRoom = entityFacade.getPrivateChatRoom(req.getPrivateChatRoomId());

        privateChatRoom.visible();

        PrivateChatMessage privateChatMessage = savePrivateChatMessage(user, privateChatRoom, req.getMessage());

        redisPublisher.publishPrivateChatMessage(PrivateChatMessageDto.of(privateChatMessage, user.getId()));
    }

    private PrivateChatMessage savePrivateChatMessage(User user, PrivateChatRoom privateChatRoom, String message) {
        PrivateChatMessage privateChatMessage = PrivateChatMessage.of(user, privateChatRoom, message);
        return privateChatMessageRepository.save(privateChatMessage);
    }

    @PreAuthorize("@chatRoomSecurity.isPrivateChatRoomMember(#userId, #privateChatRoomId)")
    @Transactional(readOnly = true)
    public InfinityScrollRes<PrivateChatMessageRes> getPrivateChatMessages(Long userId, Long privateChatRoomId, Long lastPrivateChatMessageId, int size) {
        PrivateChatRoom privateChatRoom = entityFacade.getPrivateChatRoom(privateChatRoomId);

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

    @Transactional(readOnly = true)
    public int getUnreadCnt(Long userId, Long privateChatRoomId) {
        User user = entityFacade.getUser(userId);
        PrivateChatRoom privateChatRoom = entityFacade.getPrivateChatRoom(privateChatRoomId);

        PrivateChatRoomMember privateChatRoomMember = privateChatRoomMemberService.findByUserAndPrivateChatRoom(user, privateChatRoom);
        LocalDateTime lastEntryAt = privateChatRoomMember.getLastEntryAt();

        return privateChatMessageRepository.countByPrivateChatRoomIdAndCreatedAtAfter(privateChatRoom.getId(), lastEntryAt);
    }
}
