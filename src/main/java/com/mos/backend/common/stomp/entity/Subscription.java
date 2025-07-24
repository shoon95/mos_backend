package com.mos.backend.common.stomp.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription {
    private SubscriptionType subscriptionType;
    private Long id;

    public static Subscription of(SubscriptionType subscriptionType, Long chatRoomId) {
        Subscription subscription = new Subscription();
        subscription.subscriptionType = subscriptionType;
        subscription.id = chatRoomId;
        return subscription;
    }
}
