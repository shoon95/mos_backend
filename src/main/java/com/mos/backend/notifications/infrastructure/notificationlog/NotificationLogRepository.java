package com.mos.backend.notifications.infrastructure.notificationlog;

import com.mos.backend.notifications.application.dto.NotificationResponseDto;
import com.mos.backend.notifications.entity.NotificationLog;
import com.mos.backend.notifications.entity.NotificationReadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationLogRepository {
    void save(NotificationLog notificationLog);

    Optional<NotificationLog> findById(Long notificationId);

    Integer getUnreadCount(Long userId);

    Page<NotificationResponseDto> getNotifications(Pageable pageable, Long userId, NotificationReadStatus readStatus);
}
