package com.mos.backend.notifications.presentation.controller.api;

import com.mos.backend.notifications.application.NotificationLogService;
import com.mos.backend.notifications.application.dto.NotificationResponseDto;
import com.mos.backend.notifications.application.dto.NotificationUnreadCountDto;
import com.mos.backend.notifications.entity.NotificationReadStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class NotificationLogController {
    private final NotificationLogService notificationLogService;

    /**
     * 전체 알림 조회
     * @param pageable
     * @param userId 조회할 유저 아이디
     * @param readStatus 읽음 상태 필터링
     */
    @GetMapping("/notifications")
    @ResponseStatus(HttpStatus.OK)
    public Page<NotificationResponseDto> getNotifications(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false, defaultValue = "ALL") NotificationReadStatus readStatus,
            @RequestParam Pageable pageable
    ) {
        return notificationLogService.getNotifications(pageable, userId, readStatus);
    }

    /**
     * 알림 상세 조회하며 읽음 체크
     * @param notificationId 읽을 알림의 아이디
     */
    @PostMapping("/notifications/{notificationId}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationResponseDto read(
            @PathVariable Long notificationId
    ) {
        return notificationLogService.read(notificationId);
    }

    /**
     * 사용자가 읽지 않은 알림 수 조회
     * @param userId
     */
    @GetMapping("/notifications/unread")
    public NotificationUnreadCountDto getUnreadCount(
            @AuthenticationPrincipal Long userId
    ) {
        return notificationLogService.getUnreadCount(userId);
    }
}
