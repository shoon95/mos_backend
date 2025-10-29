package com.mos.backend.common.utils;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.security.Principal;
import java.util.Objects;

public class StompPrincipalUtil {

    public static Long getUserId(StompHeaderAccessor accessor) {
        Principal user = accessor.getUser();
        return Objects.isNull(user) ? null : Long.valueOf(user.getName());
    }

    public static void validatePrincipal(StompHeaderAccessor accessor) {
        Principal user = accessor.getUser();
        if (user == null || user.getName() == null) {
            throw new JWTVerificationException("토큰이 유효하지 않습니다");
        }
    }
}
