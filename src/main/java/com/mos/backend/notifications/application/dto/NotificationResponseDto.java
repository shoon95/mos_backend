package com.mos.backend.notifications.application.dto;

import com.mos.backend.common.event.EventType;
import com.mos.backend.notifications.entity.NotificationLog;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationResponseDto {
    private Long notificationId;

    private Long recipientId;

    private EventType type;

    private String title;

    private String content;

    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationResponseDto(Long notificationId, Long recipientId, EventType type, String title, String content, boolean isRead, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.recipientId = recipientId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public NotificationResponseDto(NotificationLog notificationLog) {
        this.notificationId = notificationLog.getId();
        this.recipientId = notificationLog.getRecipient().getId();
        this.type = notificationLog.getType();
        this.title = notificationLog.getTitle();
        this.content = notificationLog.getContent();
        this.isRead = notificationLog.isRead();
        this.createdAt = notificationLog.getCreatedAt();
    }
}
