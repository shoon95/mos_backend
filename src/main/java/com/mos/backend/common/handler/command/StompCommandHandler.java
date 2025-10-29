package com.mos.backend.common.handler.command;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public interface StompCommandHandler {
    boolean isSupport(StompCommand command);

    void handle(Message<?> message, StompHeaderAccessor accessor);
}

