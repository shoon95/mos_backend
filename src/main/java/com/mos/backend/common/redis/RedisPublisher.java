package com.mos.backend.common.redis;

import com.mos.backend.privatechatmessages.application.dto.PrivateChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate redisTemplate;

    @Value("${spring.data.redis.private-chat-channel}")
    private String privateChannel;

    public void publishPrivateChatMessage(PrivateChatMessageDto privateChatMessageDto) {
        redisTemplate.convertAndSend(privateChannel, privateChatMessageDto);
    }

}