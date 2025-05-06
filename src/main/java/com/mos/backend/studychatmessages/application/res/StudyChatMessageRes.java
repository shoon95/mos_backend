package com.mos.backend.studychatmessages.application.res;

import com.mos.backend.studychatmessages.entity.StudyChatMessage;
import com.mos.backend.users.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class StudyChatMessageRes {
    private Long studyChatMessageId;
    private String message;
    private LocalDateTime messageCreatedAt;

    private Long userId;
    private String nickname;

    public static StudyChatMessageRes of(StudyChatMessage studyChatMessage) {
        User user = studyChatMessage.getUser();

        StudyChatMessageRes studyChatMessageRes = new StudyChatMessageRes();
        studyChatMessageRes.studyChatMessageId = studyChatMessage.getId();
        studyChatMessageRes.message = studyChatMessage.getMessage();
        studyChatMessageRes.messageCreatedAt = studyChatMessage.getCreatedAt();

        studyChatMessageRes.userId = user.getId();
        studyChatMessageRes.nickname = user.getNickname();

        return studyChatMessageRes;
    }
}
