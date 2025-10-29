package com.mos.backend.notifications.application;

import com.mos.backend.common.event.EventType;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.notifications.application.dto.NotificationListResponseDto;
import com.mos.backend.notifications.application.dto.NotificationResponseDto; // DTO import 추가
import com.mos.backend.notifications.application.dto.NotificationUnreadCountDto;
import com.mos.backend.notifications.entity.NotificationLog;
import com.mos.backend.notifications.entity.NotificationReadStatus;
import com.mos.backend.notifications.infrastructure.notificationlog.NotificationLogRepository;
import com.mos.backend.users.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationLogServiceTest {

    @Mock
    private NotificationLogRepository notificationLogRepository;

    @Mock
    private EntityFacade entityFacade;

    @InjectMocks
    private NotificationLogService notificationLogService;

    @Captor
    private ArgumentCaptor<NotificationLog> notificationLogCaptor;

    @Test
    @DisplayName("알림 로그 생성 성공")
    public void createTest() {
        //given
        Long userId = 1L;
        EventType type = EventType.STUDY_JOINED;
        String title = "test title";
        String content = "test content";
        User mockUser = mock(User.class);
        when(entityFacade.getUser(userId)).thenReturn(mockUser);

        // when
        notificationLogService.create(userId, type, title, content);

        // then
        verify(notificationLogRepository).save(notificationLogCaptor.capture());
        NotificationLog notificationLog = notificationLogCaptor.getValue();
        assertThat(notificationLog.getRecipient()).isEqualTo(mockUser);
        assertThat(notificationLog.getTitle()).isEqualTo(title);
        assertThat(notificationLog.getContent()).isEqualTo(content);
        assertThat(notificationLog.getType()).isEqualTo(type);
    }

    @Test
    @DisplayName("알림 읽기 성공")
    public void readTest() {
        // given
        Long notificationLogId = 1L;
        Long recipientId = 1L;

        User mockRecipient = mock(User.class);
        when(mockRecipient.getId()).thenReturn(recipientId);

        NotificationLog mockNotificationLog = mock(NotificationLog.class);
        when(entityFacade.getNotificationLog(notificationLogId)).thenReturn(mockNotificationLog);

        when(mockNotificationLog.getId()).thenReturn(notificationLogId);
        when(mockNotificationLog.getRecipient()).thenReturn(mockRecipient);
        when(mockNotificationLog.isRead()).thenReturn(true);

        // when
        NotificationResponseDto responseDto = notificationLogService.read(notificationLogId);

        // then
        verify(mockNotificationLog).read();

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getNotificationId()).isEqualTo(notificationLogId);
        assertThat(responseDto.getRecipientId()).isEqualTo(recipientId);
        assertThat(responseDto.isRead()).isTrue();
    }

    @Test
    @DisplayName("읽지 않은 알림 수 조회 성공")
    public void getUnreadCountTest() {
        // given
        Long userId = 1L;
        Integer expectedCount = 5;
        when(notificationLogRepository.getUnreadCount(userId)).thenReturn(expectedCount);

        // when
        NotificationUnreadCountDto result = notificationLogService.getUnreadCount(userId);

        // then
        verify(notificationLogRepository).getUnreadCount(userId);
        assertThat(result).isNotNull();
        assertThat(result.getUnreadCount()).isEqualTo(expectedCount);
    }

    @Test
    @DisplayName("알림 목록 조회 성공")
    public void getNotificationsTest() {
        // given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        NotificationReadStatus status = NotificationReadStatus.ALL;

        List<NotificationResponseDto> notificationList = List.of(
                new NotificationResponseDto(1L, userId, EventType.STUDY_CREATED, "Title 1", "Content 1", true, LocalDateTime.now()),
                new NotificationResponseDto(2L, userId, EventType.STUDY_JOINED, "Title 2", "Content 2", false, LocalDateTime.now())
        );
        Page<NotificationResponseDto> responsePage = new PageImpl<>(notificationList, pageable, notificationList.size());

        when(notificationLogRepository.getNotifications(pageable, userId, status)).thenReturn(responsePage);

        // when
        NotificationListResponseDto result = notificationLogService.getNotifications(pageable, userId, status);

        // then
        verify(notificationLogRepository).getNotifications(pageable, userId, status);

        assertThat(result).isNotNull();
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getNotifications()).hasSize(2);
        assertThat(result.getNotifications().get(0).getTitle()).isEqualTo("Title 1");
    }
}