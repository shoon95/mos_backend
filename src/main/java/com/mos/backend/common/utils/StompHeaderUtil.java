package com.mos.backend.common.utils;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.stomp.entity.StompErrorCode;
import com.mos.backend.common.stomp.entity.Subscription;
import com.mos.backend.common.stomp.entity.SubscriptionType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StompHeaderUtil {
    static final String USER_ID = "user-id";
    private static final Pattern DESTINATION_PATTERN = Pattern.compile("^/sub/([a-zA-Z\\-]+)/?(\\d+)?$");

    public static Long getUserId(StompHeaderAccessor accessor) {
        return Optional.ofNullable(accessor.getFirstNativeHeader(USER_ID))
                .map(Long::valueOf)
                .orElseThrow(() -> new MosException(StompErrorCode.MISSING_USER_ID_IN_HEADER));
    }

    public static Subscription parseDestination(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        if (destination == null) {
            throw new MosException(StompErrorCode.INVALID_DESTINATION);
        }

        Matcher matcher = DESTINATION_PATTERN.matcher(destination);
        if (matcher.matches()) {
            String typeStr = matcher.group(1);
            Long id = matcher.group(2) != null ? Long.valueOf(matcher.group(2)) : null;
            SubscriptionType subscriptionType = SubscriptionType.from(typeStr);
            return Subscription.of(subscriptionType, id);
        }

        throw new MosException(StompErrorCode.INVALID_DESTINATION);

    }
}
