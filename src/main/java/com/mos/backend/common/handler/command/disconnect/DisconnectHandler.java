package com.mos.backend.common.handler.command.disconnect;

import com.mos.backend.common.stomp.entity.Subscription;
import com.mos.backend.common.utils.StompPrincipalUtil;
import com.mos.backend.common.utils.StompSessionUtil;
import com.mos.backend.privatechatroommember.application.PrivateChatRoomMemberService;
import com.mos.backend.privatechatrooms.application.PrivateChatRoomInfoService;
import com.mos.backend.studychatrooms.application.StudyChatRoomInfoService;
import com.mos.backend.studymembers.application.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DisconnectHandler implements DisconnectCommandHandler {
    private final PrivateChatRoomMemberService privateChatRoomMemberService;
    private final PrivateChatRoomInfoService privateChatRoomInfoService;
    private final StudyMemberService studyMemberService;
    private final StudyChatRoomInfoService studyChatRoomInfoService;

    @Override
    public void handle(Message<?> message, StompHeaderAccessor accessor) {
        Long userId = StompPrincipalUtil.getUserId(accessor);
        if (Objects.isNull(userId)) return;

        List<Subscription> subscriptions = StompSessionUtil.getAndRemoveAllSubscription(accessor);

        subscriptions.forEach(
                subscription -> {
                    updateLastEntryAt(subscription, userId);
                    resetChatRoomInfos(subscription, userId);
                }
        );
    }

    private void updateLastEntryAt(Subscription subscription, Long userId) {
        switch (subscription.getType()) {
            case PRIVATE_CHAT_ROOM -> privateChatRoomMemberService.updateLastEntryAt(userId, subscription.getId());
            case STUDY_CHAT_ROOM -> studyMemberService.updateLastEntryAt(userId, subscription.getId());
        }
    }

    private void resetChatRoomInfos(Subscription subscription, Long userId) {
        switch (subscription.getType()) {
            case PRIVATE_CHAT_ROOM -> privateChatRoomInfoService.resetChatRoomInfos(userId);
            case STUDY_CHAT_ROOM -> studyChatRoomInfoService.resetChatRoomInfos(userId);
        }
    }
}
