package com.mos.backend.common.handler.exeption;

import com.mos.backend.common.dto.ServerSideMessage;
import com.mos.backend.common.exception.ErrorCode;
import com.mos.backend.common.exception.MosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.Principal;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class WebSocketGlobalExceptionHandler {
    private final MessageSource messageSource;
    private final SimpMessagingTemplate template;
    private static final String ERROR_DESTINATION = "/sub/errors";

    @MessageExceptionHandler(MosException.class)
    public void handleGlobalErrorException(Principal principal, MosException e) {
        ErrorCode errorCode = e.getErrorCode();

        ServerSideMessage serverSideMessage = ServerSideMessage.of(
                String.valueOf(errorCode.getStatus().value()), errorCode.getMessage(messageSource)
        );
        log.error("handleGlobalErrorException: {}", serverSideMessage);
        template.convertAndSendToUser(principal.getName(), ERROR_DESTINATION, serverSideMessage);
    }

    @MessageExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(Principal principal, RuntimeException e) {
        ServerSideMessage serverSideMessage = ServerSideMessage.of("500", e.getMessage());
        log.error("handleRuntimeException: {}", serverSideMessage);

        template.convertAndSendToUser(principal.getName(), ERROR_DESTINATION, serverSideMessage);
    }
}
