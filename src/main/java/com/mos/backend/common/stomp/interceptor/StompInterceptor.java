package com.mos.backend.common.stomp.interceptor;

import com.mos.backend.common.stomp.entity.SubscriptionType;
import com.mos.backend.common.stomp.entity.Subscription;
import com.mos.backend.common.utils.StompHeaderUtil;
import com.mos.backend.common.utils.StompSessionUtil;
import com.mos.backend.privatechatroommember.application.PrivateChatRoomMemberService;
import com.mos.backend.studymembers.application.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {

    private final PrivateChatRoomMemberService privateChatRoomMemberService;
    private final StudyMemberService studyMemberService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        StompCommand command = accessor.getCommand();
        switch (command) {
            case CONNECT -> handleConnect(accessor);
            case SUBSCRIBE -> handleSubscribe(accessor);
            case UNSUBSCRIBE -> handleUnsubscribe(accessor);
            case DISCONNECT -> handleDisconnect(accessor);
        }

        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        Long userId = StompHeaderUtil.getUserId(accessor);
        StompSessionUtil.putUserId(accessor, userId);
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        Subscription subscription = StompHeaderUtil.parseDestination(accessor);
        if (subscription.getSubscriptionType() == SubscriptionType.USER)
            return;

        StompSessionUtil.putSubscription(accessor, subscription);
    }

    private void handleUnsubscribe(StompHeaderAccessor accessor) {
        Optional<Subscription> optionalSubscription = StompSessionUtil.getAndRemoveSubscription(accessor);

        optionalSubscription.ifPresent(subscription -> {
            Long userId = StompSessionUtil.getUserId(accessor);
            updateLastEntryAt(subscription, userId);
        });
    }

    private void handleDisconnect(StompHeaderAccessor accessor) {
        Long userId = StompSessionUtil.getAndRemoveUserId(accessor);
        List<Subscription> subscriptions = StompSessionUtil.getAndRemoveAllSubscription(accessor);

        subscriptions.forEach(
                subscription -> updateLastEntryAt(subscription, userId)
        );
    }

    private void updateLastEntryAt(Subscription subscription, Long userId) {
        switch (subscription.getSubscriptionType()) {
            case PRIVATE_CHAT_ROOM -> privateChatRoomMemberService.updateLastEntryAt(userId, subscription.getId());
            case STUDY_CHAT_ROOM -> studyMemberService.updateLastEntryAt(userId, subscription.getId());
        }
    }

}