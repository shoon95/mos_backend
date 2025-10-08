package com.mos.backend.notifications.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NotificationListResponseDto {
    private long totalNotifications;
    private int currentPage;
    private int totalPages;
    private List<NotificationResponseDto> notifications;
}
