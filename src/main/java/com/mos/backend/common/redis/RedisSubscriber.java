package com.mos.backend.common.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.privatechatmessages.application.dto.PrivateChatMessageDto;
import com.mos.backend.privatechatmessages.entity.PrivateChatMessageErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final String PRIVATE_CHAT_FORMAT = "/sub/private-chat-room/%d";

    public void onPrivateChatMessage(String publishedMessage) {
        try {
            PrivateChatMessageDto privateChatMessageDto = objectMapper.readValue(publishedMessage, PrivateChatMessageDto.class);
            String destinationPath = generatePrivateDestinationPath(privateChatMessageDto.getPrivateChatRoomId());
            simpMessagingTemplate.convertAndSend(destinationPath, privateChatMessageDto);
        } catch (JsonProcessingException e) {
            throw new MosException(PrivateChatMessageErrorCode.DESERIALIZATION_FAILED);
        }
    }

    private String generatePrivateDestinationPath(Long privateChatRoomId) {
        return PRIVATE_CHAT_FORMAT.formatted(privateChatRoomId);
    }
}
