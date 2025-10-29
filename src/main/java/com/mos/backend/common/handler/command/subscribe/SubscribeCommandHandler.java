package com.mos.backend.common.handler.command.subscribe;

import com.mos.backend.common.handler.command.StompCommandHandler;
import org.springframework.messaging.simp.stomp.StompCommand;

public interface SubscribeCommandHandler extends StompCommandHandler {
    default boolean isSupport(StompCommand command) {
        return StompCommand.SUBSCRIBE.equals(command);
    }
}
