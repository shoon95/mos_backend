package com.mos.backend.studychatmessages.application.dto;

import com.mos.backend.studychatmessages.entity.StudyChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StudyChatMessageDto {
    private Long userId;
    private Long studyChatRoomId;
    private String message;
    private LocalDateTime messageCreatedAt;

    public static StudyChatMessageDto of(StudyChatMessage studyChatMessage, Long userId) {
        StudyChatMessageDto studyChatMessageDto = new StudyChatMessageDto();
        studyChatMessageDto.userId = userId;
        studyChatMessageDto.studyChatRoomId = studyChatMessage.getStudyChatRoom().getId();
        studyChatMessageDto.message = studyChatMessage.getMessage();
        studyChatMessageDto.messageCreatedAt = studyChatMessage.getCreatedAt();
        return studyChatMessageDto;
    }
}
