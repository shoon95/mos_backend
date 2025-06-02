package com.mos.backend.common.argumentresolvers.chat;

import com.mos.backend.common.annotation.Sender;
import com.mos.backend.common.jwt.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SenderArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenUtil tokenUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Sender.class) && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String accessToken = tokenUtil.extractAccessToken(accessor);
        return tokenUtil.verifyAccessToken(accessToken);
    }
}