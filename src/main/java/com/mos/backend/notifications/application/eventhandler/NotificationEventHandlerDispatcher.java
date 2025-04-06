package com.mos.backend.notifications.application.eventhandler;

import com.mos.backend.common.event.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationEventHandlerDispatcher {
    private Map<EventType, NotificationEventHandler> notificationHandlerMap = new EnumMap<>(EventType.class);

    @Autowired
    public NotificationEventHandlerDispatcher(List<NotificationEventHandler> notificationHandlerList) {
        notificationHandlerList.forEach(notificationEventHandler ->
                notificationHandlerMap.put(notificationEventHandler.support(), notificationEventHandler));
    }

    public NotificationEventHandler findNotificationHandler(EventType eventType) {
        if (!support(eventType)) {
            return null;
        } return notificationHandlerMap.get(eventType);
    }

    private boolean support(EventType eventType) {
        return notificationHandlerMap.containsKey(eventType);
    }
}
