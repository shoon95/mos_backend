package com.mos.backend.studies.application;

import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studies.infrastructure.viewcount.ViewCountRepository;
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
    private StudyRepository studyRepository;

    @Mock
    private ViewCountRepository viewCountRepository;

    @InjectMocks
    private ViewCountService viewCountService;

    private static final String TEST_IP = "127.0.0.1";
    private static final long TEST_STUDY_ID = 1L;

    @Test
    @DisplayName("새로운 IP에서 조회 시 조회수가 증가해야 한다")
    void processView_newIpAddress_shouldIncreaseViewCount() {
        // Given
        when(viewCountRepository.existsByStudyIdAndIpAddress(TEST_STUDY_ID, TEST_IP)).thenReturn(Boolean.FALSE);

        // When
        viewCountService.handleViewCount(TEST_STUDY_ID, TEST_IP);

        // Then
        verify(viewCountRepository).saveViewRecord(TEST_STUDY_ID, TEST_IP);
        verify(studyRepository).increaseViewCount(TEST_STUDY_ID);
    }

    @Test
    @DisplayName("같은 IP에서 재조회 시 조회수가 증가하지 않아야 한다")
    void processView_existingIpAddress_shouldNotIncreaseViewCount() {
        // Given
        when(viewCountRepository.existsByStudyIdAndIpAddress(TEST_STUDY_ID, TEST_IP)).thenReturn(Boolean.TRUE);

        // When
        viewCountService.handleViewCount(TEST_STUDY_ID, TEST_IP);

        // Then
        verify(viewCountRepository, never()).saveViewRecord(TEST_STUDY_ID, TEST_IP);
        verify(studyRepository, never()).increaseViewCount(TEST_STUDY_ID);
    }
}