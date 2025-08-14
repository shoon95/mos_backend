package com.mos.backend.common.handler.command.unsubscribe;

import com.mos.backend.common.handler.command.StompCommandHandler;
import org.springframework.messaging.simp.stomp.StompCommand;

public interface UnsubscribeCommandHandler extends StompCommandHandler {
    default boolean isSupport(StompCommand command) {
        return StompCommand.UNSUBSCRIBE.equals(command);
    }
}

