package com.mos.backend.studychatrooms.application;

import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studychatrooms.entity.StudyChatRoom;
import com.mos.backend.studychatrooms.infrastructure.StudyChatRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudyChatRoomService 테스트")
class StudyChatRoomServiceTest {

    @Mock
    private EntityFacade entityFacade;
    @Mock
    private StudyChatRoomRepository studyChatRoomRepository;

    @InjectMocks
    private StudyChatRoomService studyChatRoomService;

    @Nested
    @DisplayName("스터디 채팅방 생성 성공 시나리오")
    class CreateStudyChatRoomSuccessScenarios {
        @Test
        @DisplayName("스터디 채팅방 생성 성공")
        void createStudyChatRoom_Success() {
            // Given
            Long studyId = 1L;
            Study study = mock(Study.class);

            when(entityFacade.getStudy(studyId)).thenReturn(study);

            // When
            studyChatRoomService.create(studyId);

            // Then
            verify(studyChatRoomRepository).save(any(StudyChatRoom.class));
        }
    }
}