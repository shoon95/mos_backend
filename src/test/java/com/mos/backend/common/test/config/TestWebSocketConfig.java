package com.mos.backend.common.test.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.mock;

/**
 * 테스트 환경에서 WebSocket 관련 의존성 문제를 해결하기 위한 설정 클래스
 */
@TestConfiguration
public class TestWebSocketConfig {

    /**
     * 테스트 환경에서 SimpMessagingTemplate의 Mock 빈을 제공합니다.
     * WebSocketGlobalExceptionHandler의 의존성 문제를 해결하기 위함입니다.
     */
    @Bean
    @Primary
    public SimpMessagingTemplate simpMessagingTemplate() {
        return mock(SimpMessagingTemplate.class);
    }
}
