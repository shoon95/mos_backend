package com.mos.backend.common.stomp.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mos.backend.common.dto.ServerSideMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticateExceptionHandler extends AbstractStompExceptionHandler {
    public AuthenticateExceptionHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public boolean canHandle(Throwable cause) {
        return cause instanceof JWTVerificationException;
    }

    @Override
    protected StompCommand getStompCommand() {
        return StompCommand.ERROR;
    }

    @Override
    protected ServerSideMessage getServerSideMessage(Throwable cause) {
        JWTVerificationException e = (JWTVerificationException) cause;

        log.warn("[JWT 인증 예외] {}", e.getMessage());

        return ServerSideMessage.of(String.valueOf(HttpStatus.UNAUTHORIZED.value()), e.getMessage());
    }
}
