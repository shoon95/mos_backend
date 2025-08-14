package com.mos.backend.common.handler.command.connect;

import com.mos.backend.common.utils.StompPrincipalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticateHandler implements ConnectCommandHandler {

    @Override
    public void handle(Message<?> message, StompHeaderAccessor accessor) {
        StompPrincipalUtil.validatePrincipal(accessor);
    }
}
