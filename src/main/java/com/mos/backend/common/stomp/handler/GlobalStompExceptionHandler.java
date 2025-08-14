package com.mos.backend.common.stomp.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mos.backend.common.dto.ServerSideMessage;
import com.mos.backend.common.exception.ErrorCode;
import com.mos.backend.common.exception.MosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GlobalStompExceptionHandler extends AbstractStompExceptionHandler {
    private final MessageSource messageSource;

    public GlobalStompExceptionHandler(ObjectMapper objectMapper, MessageSource messageSource) {
        super(objectMapper);
        this.messageSource = messageSource;
    }

    @Override
    public boolean canHandle(Throwable cause) {
        return cause instanceof MosException;
    }

    @Override
    protected StompCommand getStompCommand() {
        return StompCommand.SEND;
    }

    @Override
    protected ServerSideMessage getServerSideMessage(Throwable cause) {
        MosException e = (MosException) cause;

        ErrorCode errorCode = e.getErrorCode();
        log.warn("[GlobalException] {}", e.getMessage());

        return ServerSideMessage.of(String.valueOf(errorCode.getErrorName()), errorCode.getMessage(messageSource));
    }
}