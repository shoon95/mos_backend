package com.mos.backend.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisPrivateChatRoomUtil {
    private final RedisTemplate<String, Long> redisTemplate;

    private static final String KEY_FORMAT = "PRIVATE-CHAT-ROOM::%d";

    public void enterChatRoom(Long chatRoomId, Long userId) {
        SetOperations<String, Long> setOperations = redisTemplate.opsForSet();
        setOperations.add(KEY_FORMAT.formatted(chatRoomId), userId);
    }

    public void leaveChatRoom(Long chatRoomId, Long userId) {
        SetOperations<String, Long> setOperations = redisTemplate.opsForSet();
        setOperations.remove(KEY_FORMAT.formatted(chatRoomId), userId);
    }
}
