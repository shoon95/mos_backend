package com.mos.backend.common.handler.command.subscribe;

import com.mos.backend.common.stomp.entity.Subscription;
import com.mos.backend.common.utils.StompHeaderUtil;
import com.mos.backend.common.utils.StompSessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class SubscribeHandler implements SubscribeCommandHandler {
    private static final String USER_DESTINATION_PREFIX = "/user";

    @Override
    public void handle(Message<?> message, StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        if (Objects.nonNull(destination) && destination.startsWith(USER_DESTINATION_PREFIX)) return;

        Subscription subscription = StompHeaderUtil.parseDestination(destination);
        StompSessionUtil.putSubscription(accessor, subscription);
    }

}
