package com.mos.backend.common.redis;

import com.mos.backend.privatechatmessages.application.dto.PrivateChatMessageDto;
import com.mos.backend.studychatmessages.application.dto.StudyChatMessageDto;
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
    @Value("${spring.data.redis.study-chat-channel}")
    private String studyChannel;

    public void publishPrivateChatMessage(PrivateChatMessageDto privateChatMessageDto) {
        redisTemplate.convertAndSend(privateChannel, privateChatMessageDto);
    }

    public void publishStudyChatMessage(StudyChatMessageDto studyChatMessageDto) {
        redisTemplate.convertAndSend(studyChannel, studyChatMessageDto);
    }
}