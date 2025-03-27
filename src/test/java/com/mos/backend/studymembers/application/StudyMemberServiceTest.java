package com.mos.backend.studymembers.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studymembers.entity.ParticipationStatus;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studymembers.entity.StudyMemberRoleType;
import com.mos.backend.studymembers.entity.exception.StudyMemberErrorCode;
import com.mos.backend.studymembers.infrastructure.StudyMemberRepository;
import com.mos.backend.users.entity.User;
import com.mos.backend.users.entity.exception.UserErrorCode;
import com.mos.backend.users.infrastructure.respository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudyMemberService 테스트")
class StudyMemberServiceTest {
    @Mock
    private StudyRepository studyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StudyMemberRepository studyMemberRepository;
    @Mock
    private EntityFacade entityFacade;
    @InjectMocks
    private StudyMemberService studyMemberService;

    @Nested
    @DisplayName("스터디 리더 생성 성공 시나리오")
    class CreateLeaderSuccessScenarios {
        @Test
        @DisplayName("스터디 리더 생성 성공")
        void createStudyLeader_Success() {
            // Given
            Long studyId = 1L;
            Long userId = 1L;
            Study mockStudy = mock(Study.class);
            User mockUser = mock(User.class);

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(studyMemberRepository.existsByStudyAndRoleType(mockStudy, StudyMemberRoleType.LEADER)).thenReturn(false);

            // When
            studyMemberService.createStudyLeader(studyId, userId);

            // Then
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getUser(userId);
            verify(studyMemberRepository).save(any(StudyMember.class));
        }
    }

    @Nested
    @DisplayName("스터디 리더 생성 실패 시나리오")
    class CreateLeaderFailureScenarios {
        @Test
        @DisplayName("스터디 리더가 이미 존재할 때 MosException 발생")
        void createStudyLeader_StudyLeaderAlreadyExist() {
            // Given
            Long studyId = 1L;
            Long userId = 1L;
            Study mockStudy = mock(Study.class);
            User mockUser = mock(User.class);

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(studyMemberRepository.existsByStudyAndRoleType(mockStudy, StudyMemberRoleType.LEADER)).thenReturn(true);

            // When
            MosException exception = assertThrows(MosException.class, () -> {
                studyMemberService.createStudyLeader(studyId, userId);
            });

            // Then
            assertEquals(StudyMemberErrorCode.STUDY_LEADER_ALREADY_EXIST, exception.getErrorCode());
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getUser(userId);
            verify(studyMemberRepository, never()).save(any(StudyMember.class));
        }
    }

    @Nested
    @DisplayName("스터디원 생성 성공 시나리오")
    class CreateSuccessScenarios {
        @Test
        @DisplayName("스터디원 생성 성공")
        void createStudyMember_Success() {
            // Given
            Long studyId = 1L;
            Long userId = 1L;
            Study mockStudy = mock(Study.class);
            User mockUser = mock(User.class);
            List<ParticipationStatus> currentParticipationStatusList = Arrays.asList(ParticipationStatus.ACTIVATED, ParticipationStatus.COMPLETED);

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(studyMemberRepository.countByStudyAndStatusIn(mockStudy, currentParticipationStatusList)).thenReturn(1);
            when(mockStudy.getMaxStudyMemberCount()).thenReturn(5);

            // When
            studyMemberService.createStudyMember(studyId, userId);

            // Then
            verify(entityFacade, times(2)).getStudy(studyId);
            verify(entityFacade).getUser(userId);
            verify(studyMemberRepository).save(any(StudyMember.class));
        }
    }

    @Nested
    @DisplayName("스터디원 생성 실패 시나리오")
    class CreateFailureScenarios {
        @Test
        @DisplayName("스터디가 존재하지 않을 때 MosException 발생")
        void createStudyMember_StudyNotFound() {
            // Given
            Long studyId = 1L;
            Long userId = 1L;

            when(entityFacade.getStudy(studyId)).thenThrow(new MosException(StudyErrorCode.STUDY_NOT_FOUND));

            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyMemberService.createStudyLeader(studyId, userId);
            });

            assertEquals(StudyErrorCode.STUDY_NOT_FOUND, exception.getErrorCode());
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade, never()).getUser(anyLong());
            verify(studyMemberRepository, never()).save(any(StudyMember.class));
        }

        @Test
        @DisplayName("유저가 존재하지 않을 때 MosException 발생")
        void createStudyMember_UserNotFound() {
            // Given
            Long studyId = 1L;
            Long userId = 1L;
            Study mockStudy = mock(Study.class);

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getUser(userId)).thenThrow(new MosException(UserErrorCode.USER_NOT_FOUND));

            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyMemberService.createStudyLeader(studyId, userId);
            });

            assertEquals(UserErrorCode.USER_NOT_FOUND, exception.getErrorCode());
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getUser(userId);
            verify(studyMemberRepository, never()).save(any(StudyMember.class));
        }

        @Test
        @DisplayName("스터디 멤버 수가 0일 때 MosException 발생")
        void createStudyMember_StudyMemberCountZero() {
            // Given
            Long studyId = 1L;
            Long userId = 1L;
            Study mockStudy = mock(Study.class);
            User mockUser = mock(User.class);
            List<ParticipationStatus> currentParticipationStatusList = Arrays.asList(ParticipationStatus.ACTIVATED, ParticipationStatus.COMPLETED);

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(studyMemberRepository.countByStudyAndStatusIn(mockStudy, currentParticipationStatusList)).thenReturn(0);

            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyMemberService.createStudyMember(studyId, userId);
            });

            assertEquals(StudyErrorCode.STUDY_NOT_FOUND, exception.getErrorCode());
            verify(entityFacade, times(2)).getStudy(studyId);
            verify(entityFacade).getUser(userId);
            verify(studyMemberRepository, never()).save(any(StudyMember.class));
        }

        @Test
        @DisplayName("스터디 멤버 수가 최대 스터디 멤버 수 이상일 때 MosException 발생")
        void createStudyMember_StudyMemberCountOver() {
            // Given
            Long studyId = 1L;
            Long userId = 1L;
            Study mockStudy = mock(Study.class);
            User mockUser = mock(User.class);
            List<ParticipationStatus> currentParticipationStatusList = Arrays.asList(ParticipationStatus.ACTIVATED, ParticipationStatus.COMPLETED);

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(studyMemberRepository.countByStudyAndStatusIn(mockStudy, currentParticipationStatusList)).thenReturn(5);
            when(mockStudy.getMaxStudyMemberCount()).thenReturn(5);

            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyMemberService.createStudyMember(studyId, userId);
            });

            assertEquals(StudyMemberErrorCode.STUDY_MEMBER_FULL, exception.getErrorCode());
            verify(entityFacade, times(2)).getStudy(studyId);
            verify(entityFacade).getUser(userId);
            verify(studyMemberRepository, never()).save(any(StudyMember.class));
        }
    }
}
