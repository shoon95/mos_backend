package com.mos.backend.privatechatrooms.application;

import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.common.redis.RedisPrivateChatRoomUtil;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrivateChatRoomService {
    private final RedisPrivateChatRoomUtil redisPrivateChatRoomUtil;
    private final EntityFacade entityFacade;

    public void enter(Long userId, Long privateChatRoomId) {
        User user = entityFacade.getUser(userId);
        PrivateChatRoom privateChatRoom = entityFacade.getPrivateChatRoom(privateChatRoomId);
        redisPrivateChatRoomUtil.enterChatRoom(user.getId(), privateChatRoom.getId());
    }

    public void leave(Long userId, Long privateChatRoomId) {
        User user = entityFacade.getUser(userId);
        PrivateChatRoom privateChatRoom = entityFacade.getPrivateChatRoom(privateChatRoomId);
        redisPrivateChatRoomUtil.leaveChatRoom(user.getId(), privateChatRoom.getId());
    }
}
