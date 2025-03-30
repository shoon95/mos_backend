package com.mos.backend.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

@AllArgsConstructor
@Getter
public class Event<T extends Payload> implements ResolvableTypeProvider {

    private EventType eventType;
    private T payload;

    public static <T extends Payload> Event<T> create(EventType eventType, T payload) {
        return new Event<>(eventType, payload);
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getPayload()));
    }
}
