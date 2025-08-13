package com.mos.backend.common.handler.command.unsubscribe;

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

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UnsubscribeHandler implements UnsubscribeCommandHandler {
    private final PrivateChatRoomMemberService privateChatRoomMemberService;
    private final PrivateChatRoomInfoService privateChatRoomInfoService;
    private final StudyMemberService studyMemberService;
    private final StudyChatRoomInfoService studyChatRoomInfoService;

    @Override
    public void handle(Message<?> message, StompHeaderAccessor accessor) {
        Optional<Subscription> optionalSubscription = StompSessionUtil.getAndRemoveSubscription(accessor);

        optionalSubscription.ifPresent(subscription -> {
            Long userId = StompPrincipalUtil.getUserId(accessor);
            updateLastEntryAt(subscription, userId);
            resetUnreadCount(subscription, userId);
        });
    }

    private void updateLastEntryAt(Subscription subscription, Long userId) {
        switch (subscription.getType()) {
            case PRIVATE_CHAT_ROOM -> privateChatRoomMemberService.updateLastEntryAt(userId, subscription.getId());
            case STUDY_CHAT_ROOM -> studyMemberService.updateLastEntryAt(userId, subscription.getId());
        }
    }

    private void resetUnreadCount(Subscription subscription, Long userId) {
        switch (subscription.getType()) {
            case PRIVATE_CHAT_ROOM -> privateChatRoomInfoService.resetUnreadCount(userId, subscription.getId());
            case STUDY_CHAT_ROOM -> studyChatRoomInfoService.resetUnreadCount(userId, subscription.getId());
        }
    }
}
