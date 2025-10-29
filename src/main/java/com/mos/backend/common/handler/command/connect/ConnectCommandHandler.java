package com.mos.backend.common.handler.command.connect;

import com.mos.backend.common.handler.command.StompCommandHandler;
import org.springframework.messaging.simp.stomp.StompCommand;

public interface ConnectCommandHandler extends StompCommandHandler {
    default boolean isSupport(StompCommand command) {
        return StompCommand.CONNECT.equals(command);
    }
}

