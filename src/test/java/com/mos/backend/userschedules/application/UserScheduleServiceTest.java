package com.mos.backend.userschedules.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.users.entity.User;
import com.mos.backend.users.entity.exception.UserErrorCode;
import com.mos.backend.userschedules.entity.UserSchedule;
import com.mos.backend.userschedules.infrastructure.UserScheduleRepository;
import com.mos.backend.userschedules.presentation.req.UserScheduleCreateReq;
import com.mos.backend.userschedules.presentation.req.UserScheduleUpdateReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserScheduleService 테스트")
class UserScheduleServiceTest {
    @Mock
    private UserScheduleRepository userScheduleRepository;
    @Mock
    private EntityFacade entityFacade;
    @InjectMocks
    private UserScheduleService userScheduleService;

    @Nested
    @DisplayName("유저 일정 생성 성공 시나리오")
    class CreateUserScheduleSuccessScenario {
        @Test
        @DisplayName("유저 일정 생성 성공")
        void createStudySchedule_Success() {
            // Given
            Long userId = 1L;
            User user = mock(User.class);

            when(entityFacade.getUser(userId)).thenReturn(user);

            // When
            userScheduleService.createUserSchedule(userId, mock(UserScheduleCreateReq.class));

            // Then
            verify(entityFacade).getUser(userId);
            verify(userScheduleRepository).save(any());
        }
    }

    @Nested
    @DisplayName("유저 일정 조회 성공 시나리오")
    class GetUserSchedulesSuccessScenario {
        @Test
        @DisplayName("유저 일정 조회 성공")
        void getUserSchedules_Success() {
            // Given
            Long userId = 1L;
            User user = mock(User.class);

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(userScheduleRepository.findByUserId(user.getId())).thenReturn(List.of(mock(UserSchedule.class)));

            // When
            userScheduleService.getUserSchedules(userId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(userScheduleRepository).findByUserId(user.getId());
        }
    }

    @Nested
    @DisplayName("유저 일정 수정 성공 시나리오")
    class UpdateUserScheduleSuccessScenario {
        @Test
        @DisplayName("유저 일정 수정 성공")
        void updateUserSchedule_Success() {
            // Given
            Long userId = 1L;
            Long userScheduleId = 1L;
            User user = mock(User.class);
            UserSchedule userSchedule = mock(UserSchedule.class);

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getUserSchedule(userScheduleId)).thenReturn(userSchedule);
            when(userSchedule.getUser()).thenReturn(user);
            when(user.isOwner(user.getId())).thenReturn(true);

            // When
            userScheduleService.updateUserSchedule(userId, userScheduleId, mock(UserScheduleUpdateReq.class));

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getUserSchedule(userScheduleId);
            verify(userSchedule).update(any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("유저 일정 수정 실패 시나리오")
    class UpdateUserScheduleFailScenario {
        @Test
        @DisplayName("유저의 일정이 아닌 경우 MosException 발생")
        void updateUserSchedule_Fail_Unauthorized() {
            // Given
            Long userId = 1L;
            Long userScheduleId = 1L;
            User user = mock(User.class);
            UserSchedule userSchedule = mock(UserSchedule.class);

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getUserSchedule(userScheduleId)).thenReturn(userSchedule);
            when(userSchedule.getUser()).thenReturn(user);
            when(user.isOwner(user.getId())).thenReturn(false);

            // When
            MosException exception = assertThrows(MosException.class, () -> userScheduleService.updateUserSchedule(userId, userScheduleId, mock(UserScheduleUpdateReq.class)));
            // Then
            assertEquals(exception.getErrorCode(), UserErrorCode.USER_FORBIDDEN);
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getUserSchedule(userScheduleId);
            verify(userSchedule, never()).update(any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("유저 일정 삭제 성공 시나리오")
    class DeleteUserScheduleSuccessScenario {
        @Test
        @DisplayName("유저 일정 삭제 성공")
        void deleteUserSchedule_Success() {
            // Given
            Long userId = 1L;
            Long userScheduleId = 1L;
            User user = mock(User.class);
            UserSchedule userSchedule = mock(UserSchedule.class);

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getUserSchedule(userScheduleId)).thenReturn(userSchedule);
            when(userSchedule.getUser()).thenReturn(user);
            when(user.isOwner(user.getId())).thenReturn(true);

            // When
            userScheduleService.deleteUserSchedule(userId, userScheduleId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getUserSchedule(userScheduleId);
            verify(userScheduleRepository).delete(userSchedule);
        }
    }

    @Nested
    @DisplayName("유저 일정 삭제 실패 시나리오")
    class DeleteUserScheduleFailScenario {
        @Test
        @DisplayName("유저의 일정이 아닌 경우 MosException 발생")
        void deleteUserSchedule_Fail_Unauthorized() {
            // Given
            Long userId = 1L;
            Long userScheduleId = 1L;
            User user = mock(User.class);
            UserSchedule userSchedule = mock(UserSchedule.class);

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getUserSchedule(userScheduleId)).thenReturn(userSchedule);
            when(userSchedule.getUser()).thenReturn(user);
            when(user.isOwner(user.getId())).thenReturn(false);

            // When
            MosException exception = assertThrows(MosException.class, () -> userScheduleService.deleteUserSchedule(userId, userScheduleId));
            // Then
            assertEquals(exception.getErrorCode(), UserErrorCode.USER_FORBIDDEN);
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getUserSchedule(userScheduleId);
            verify(userScheduleRepository, never()).delete(userSchedule);
        }
    }
}
