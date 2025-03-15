package com.mos.backend.studies.application;

import com.mos.backend.studies.infrastructure.StudyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ViewCountServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private StudyRepository studyRepository;

    @Mock
    private SetOperations<String, String> setOperations;

    @InjectMocks
    private ViewCountService viewCountService;

    private static final String TEST_IP = "127.0.0.1";
    private static final long TEST_STUDY_ID = 1L;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForSet()).thenReturn(setOperations);
    }

    @Test
    @DisplayName("새로운 IP에서 조회 시 조회수가 증가해야 한다")
    void processView_newIpAddress_shouldIncreaseViewCount() {
        // Given
        when(setOperations.isMember(anyString(), eq(TEST_IP))).thenReturn(false);

        // When
        viewCountService.handleViewCount(TEST_STUDY_ID, TEST_IP);

        // Then
        verify(setOperations).add(anyString(), eq(TEST_IP));
        verify(redisTemplate).expire(anyString(), eq(600L), eq(TimeUnit.SECONDS));
        verify(studyRepository).increaseViewCount(TEST_STUDY_ID);
    }

    @Test
    @DisplayName("같은 IP에서 재조회 시 조회수가 증가하지 않아야 한다")
    void processView_existingIpAddress_shouldNotIncreaseViewCount() {
        // Given
        when(setOperations.isMember(anyString(), eq(TEST_IP))).thenReturn(true);

        // When
        viewCountService.handleViewCount(TEST_STUDY_ID, TEST_IP);

        // Then
        verify(setOperations, never()).add(anyString(), anyString());
        verify(redisTemplate, never()).expire(anyString(), anyLong(), any(TimeUnit.class));
        verify(studyRepository, never()).increaseViewCount(anyLong());
    }

    @Test
    @DisplayName("isCountable 메서드가 새로운 IP는 true를 반환해야 한다")
    void isCountable_newIpAddress_shouldReturnTrue() {
        // Given
        when(setOperations.isMember(anyString(), eq(TEST_IP))).thenReturn(false);

        // When
        boolean result = viewCountService.isCountable(TEST_STUDY_ID, TEST_IP);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("isCountable 메서드가 기존 IP는 false를 반환해야 한다")
    void isCountable_existingIpAddress_shouldReturnFalse() {
        // Given
        when(setOperations.isMember(anyString(), eq(TEST_IP))).thenReturn(true);

        // When
        boolean result = viewCountService.isCountable(TEST_STUDY_ID, TEST_IP);

        // Then
        assertFalse(result);
    }
}