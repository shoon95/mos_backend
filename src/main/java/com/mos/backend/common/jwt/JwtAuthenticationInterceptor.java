package com.mos.backend.common.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements ChannelInterceptor {

    private final TokenUtil tokenUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == StompCommand.CONNECT)
            handleConnectCommand(accessor);

        return message;
    }

    private void handleConnectCommand(StompHeaderAccessor accessor) {
        validateToken(accessor);
    }

    private void validateToken(StompHeaderAccessor accessor) {
        String accessToken = tokenUtil.extractAccessToken(accessor);
        DecodedJWT decodedJWT = tokenUtil.decodedJWT(accessToken);
        decodedJWT.getClaim("id").asLong();
    }
}