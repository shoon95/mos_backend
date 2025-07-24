package com.mos.backend.common.utils;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.stomp.entity.StompErrorCode;
import com.mos.backend.common.stomp.entity.Subscription;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StompSessionUtil {
    static final String USER_ID = "USER_ID";
    static final String SUBSCRIPTIONS = "SUBSCRIPTIONS";

    public static void putUserId(StompHeaderAccessor accessor, Long userId) {
        accessor.getSessionAttributes().put(USER_ID, userId);
    }

    public static Long getUserId(StompHeaderAccessor accessor) {
        return Optional.ofNullable((Long) accessor.getSessionAttributes().get(USER_ID))
                .orElseThrow(() -> new MosException(StompErrorCode.MISSING_USER_ID_IN_SESSION));
    }

    public static Long getAndRemoveUserId(StompHeaderAccessor accessor) {
        return (Long) accessor.getSessionAttributes().remove(USER_ID);
    }

    public static void putSubscription(StompHeaderAccessor accessor, Subscription subscription) {
        String subscriptionId = accessor.getSubscriptionId();

        Map<String, Subscription> subscriptionMap = (Map<String, Subscription>) accessor.getSessionAttributes().computeIfAbsent(
                SUBSCRIPTIONS, key -> new HashMap<>()
        );
        subscriptionMap.put(subscriptionId, subscription);
    }

    public static Optional<Subscription> getAndRemoveSubscription(StompHeaderAccessor accessor) {
        String subscriptionId = accessor.getSubscriptionId();

        Map<String, Subscription> subscriptionMap = (Map<String, Subscription>) accessor.getSessionAttributes().get(SUBSCRIPTIONS);
        return Optional.ofNullable(subscriptionMap.remove(subscriptionId));
    }

    public static List<Subscription> getAndRemoveAllSubscription(StompHeaderAccessor accessor) {
        Map<String, Subscription> subscriptionMap = (Map<String, Subscription>) accessor.getSessionAttributes().remove(SUBSCRIPTIONS);
        if (subscriptionMap == null) {
            return List.of();
        }
        return subscriptionMap.values().stream().toList();
    }
}
