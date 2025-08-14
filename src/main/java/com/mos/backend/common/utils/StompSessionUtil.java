package com.mos.backend.common.utils;

import com.mos.backend.common.stomp.entity.Subscription;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.*;

public class StompSessionUtil {
    static final String SUBSCRIPTIONS = "SUBSCRIPTIONS";

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
        return Optional.ofNullable(Objects.isNull(subscriptionMap) ? null : subscriptionMap.remove(subscriptionId));
    }

    public static List<Subscription> getAndRemoveAllSubscription(StompHeaderAccessor accessor) {
        Map<String, Subscription> subscriptionMap = (Map<String, Subscription>) accessor.getSessionAttributes().remove(SUBSCRIPTIONS);
        if (subscriptionMap == null) {
            return List.of();
        }
        return subscriptionMap.values().stream().toList();
    }
}
