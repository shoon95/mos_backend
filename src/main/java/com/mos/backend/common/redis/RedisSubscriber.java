package com.mos.backend.common.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.privatechatmessages.application.dto.PrivateChatMessageDto;
import com.mos.backend.privatechatmessages.entity.PrivateChatMessageErrorCode;
import com.mos.backend.studychatmessages.application.dto.StudyChatMessageDto;
import com.mos.backend.studychatmessages.entity.exception.StudyChatMessageErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final String PRIVATE_CHAT_FORMAT = "/sub/private-chat-room/%d";
    private final String STUDY_CHAT_FORMAT = "/sub/study-chat-room/%d";

    public void onPrivateChatMessage(String publishedMessage) {
        try {
            PrivateChatMessageDto privateChatMessageDto = objectMapper.readValue(publishedMessage, PrivateChatMessageDto.class);
            String destinationPath = generatePrivateDestinationPath(privateChatMessageDto.getPrivateChatRoomId());
            simpMessagingTemplate.convertAndSend(destinationPath, privateChatMessageDto);
        } catch (JsonProcessingException e) {
            throw new MosException(PrivateChatMessageErrorCode.DESERIALIZATION_FAILED);
        }
    }

    public void onStudyChatMessage(String publishedMessage) {
        try {
            StudyChatMessageDto studyChatMessageDto = objectMapper.readValue(publishedMessage, StudyChatMessageDto.class);
            String destinationPath = generateStudyDestinationPath(studyChatMessageDto.getStudyChatRoomId());
            simpMessagingTemplate.convertAndSend(destinationPath, studyChatMessageDto);
        } catch (JsonProcessingException e) {
            throw new MosException(StudyChatMessageErrorCode.DESERIALIZATION_FAILED);
        }
    }

    private String generatePrivateDestinationPath(Long privateChatRoomId) {
        return PRIVATE_CHAT_FORMAT.formatted(privateChatRoomId);
    }

    private String generateStudyDestinationPath(Long studyChatRoomId) {
        return STUDY_CHAT_FORMAT.formatted(studyChatRoomId);
    }
}
