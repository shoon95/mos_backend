package com.mos.backend.studyjoins.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.questionanswers.infrastructure.QuestionAnswerRepository;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studyjoins.application.res.MyStudyJoinRes;
import com.mos.backend.studyjoins.application.res.QuestionAnswerRes;
import com.mos.backend.studyjoins.application.res.StudyJoinRes;
import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studyjoins.entity.StudyJoinStatus;
import com.mos.backend.studyjoins.entity.exception.StudyJoinErrorCode;
import com.mos.backend.studyjoins.infrastructure.StudyJoinRepository;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studymembers.entity.exception.StudyMemberErrorCode;
import com.mos.backend.studymembers.infrastructure.StudyMemberRepository;
import com.mos.backend.users.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
@DisplayName("StudyApplicationService 테스트")
public class StudyJoinServiceTest {

    @Mock
    private StudyMemberRepository studyMemberRepository;
    @Mock
    private StudyJoinRepository studyJoinRepository;
    @Mock
    private QuestionAnswerRepository questionAnswerRepository;
    @Mock
    private EntityFacade entityFacade;
    @InjectMocks
    private StudyJoinService studyJoinService;

    @Nested
    @DisplayName("스터디의 스터디 신청 목록 조회 성공 시나리오")
    class GetStudyJoinSuccessScenarios {
        @Test
        @DisplayName("스터디의 스터디 신청 목록 조회")
        void getStudyJoins_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyJoinId1 = 1L;
            Long studyJoinId2 = 2L;
            User user = mock(User.class);
            Study mockStudy = mock(Study.class);
            StudyJoin mockStudyJoin1 = mock(StudyJoin.class);
            StudyJoin mockStudyJoin2 = mock(StudyJoin.class);
            List<StudyJoin> mockStudyJoins = List.of(mockStudyJoin1, mockStudyJoin2);
            QuestionAnswerRes questionAnswerRes1 = mock(QuestionAnswerRes.class);
            QuestionAnswerRes questionAnswerRes2 = mock(QuestionAnswerRes.class);
            List<QuestionAnswerRes> mockQuestionAnswers = List.of(questionAnswerRes1, questionAnswerRes2);

            when(mockStudyJoin1.getId()).thenReturn(studyJoinId1);
            when(mockStudyJoin2.getId()).thenReturn(studyJoinId2);
            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(mockStudy.getId()).thenReturn(studyId);
            when(studyJoinRepository.findAllByStudyId(studyId)).thenReturn(mockStudyJoins);
            when(questionAnswerRepository.findAllByStudyJoinId(studyJoinId1)).thenReturn(mockQuestionAnswers);
            when(questionAnswerRepository.findAllByStudyJoinId(studyJoinId2)).thenReturn(mockQuestionAnswers);
            when(mockStudyJoin1.getUser()).thenReturn(user);
            when(mockStudyJoin2.getUser()).thenReturn(user);

            // When
            List<StudyJoinRes> studyJoinResList = studyJoinService.getStudyJoins(userId, studyId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(studyJoinRepository).findAllByStudyId(studyId);
            verify(questionAnswerRepository).findAllByStudyJoinId(1L);
            verify(questionAnswerRepository).findAllByStudyJoinId(2L);
            assertNotNull(studyJoinResList);
            assertEquals(2, studyJoinResList.size());
        }
    }

    @Nested
    @DisplayName("나의 스터디 신청 조회 성공 시나리오")
    class GetMyStudyJoinSuccessScenarios {
        @Test
        @DisplayName("나의 스터디 신청 조회")
        void getMyStudyJoin_Success() {
            // Given
            Long userId = 1L;
            User user = mock(User.class);
            Study mockStudy1 = mock(Study.class);
            StudyJoin mockStudyJoin1 = mock(StudyJoin.class);
            Study mockStudy2 = mock(Study.class);
            StudyJoin mockStudyJoin2 = mock(StudyJoin.class);
            List<StudyJoin> mockStudyJoins = List.of(mockStudyJoin1, mockStudyJoin2);
            StudyJoinStatus status = StudyJoinStatus.PENDING;

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(mockStudyJoin1.getStudy()).thenReturn(mockStudy1);
            when(mockStudyJoin2.getStudy()).thenReturn(mockStudy2);
            when(studyJoinRepository.findAllByStatusWithStudy(status)).thenReturn(mockStudyJoins);

            // When
            List<MyStudyJoinRes> myStudyJoinResList = studyJoinService.getMyStudyJoins(userId, status);

            // Then
            verify(entityFacade).getUser(userId);
            verify(studyJoinRepository).findAllByStatusWithStudy(status);
            assertThat(myStudyJoinResList).hasSize(mockStudyJoins.size());
        }
    }

    @Nested
    @DisplayName("스터디 신청 승인 성공 시나리오")
    class ApproveSuccessScenarios {
        @Test
        @DisplayName("스터디 신청 승인")
        void approveStudyMember_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyJoinId = 1L;
            User mockUser = mock(User.class);
            Study mockStudy = mock(Study.class);
            StudyJoin mockStudyJoin = mock(StudyJoin.class);

            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getStudyJoin(studyJoinId)).thenReturn(mockStudyJoin);
            when(mockStudyJoin.isSameStudy(mockStudy)).thenReturn(true);
            when(studyMemberRepository.countByStudy(mockStudy)).thenReturn(1L);
            when(mockStudy.getMaxStudyMemberCount()).thenReturn(5);

            // When
            studyJoinService.approveStudyJoin(userId, studyId, studyJoinId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudyJoin(studyJoinId);
            verify(mockStudyJoin).isSameStudy(mockStudy);
            verify(mockStudyJoin).approve();
            verify(studyMemberRepository).save(any(StudyMember.class));
        }
    }

    @Nested
    @DisplayName("스터디 신청 승인 실패 시나리오")
    class ApproveFailureScenarios {
        @Test
        @DisplayName("스터디가 존재하지 않을 때 MosException 발생")
        void approveStudyMember_StudyNotFound() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyJoinId = 1L;
            User mockUser = mock(User.class);
            Study mockStudy = mock(Study.class);
            StudyJoin mockStudyJoin = mock(StudyJoin.class);

            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(entityFacade.getStudy(studyId)).thenThrow(new MosException(StudyErrorCode.STUDY_NOT_FOUND));

            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyJoinService.approveStudyJoin(userId, studyId, studyJoinId);
            });

            assertEquals(StudyErrorCode.STUDY_NOT_FOUND, exception.getErrorCode());

            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade, never()).getStudyJoin(studyJoinId);
            verify(mockStudyJoin, never()).isSameStudy(mockStudy);
            verify(mockStudyJoin, never()).approve();
            verify(studyMemberRepository, never()).save(any(StudyMember.class));
        }

        @Test
        @DisplayName("스터디 멤버가 가득 찼을 때 MosException 발생")
        void approveStudyMember_StudyMemberFull() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyJoinId = 1L;
            User mockUser = mock(User.class);
            Study mockStudy = mock(Study.class);
            StudyJoin mockStudyJoin = mock(StudyJoin.class);

            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getStudyJoin(studyJoinId)).thenReturn(mockStudyJoin);
            when(mockStudyJoin.isSameStudy(mockStudy)).thenReturn(true);
            when(studyMemberRepository.countByStudy(mockStudy)).thenReturn(4L);
            when(mockStudy.getMaxStudyMemberCount()).thenReturn(4);

            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyJoinService.approveStudyJoin(userId, studyId, studyJoinId);
            });

            assertEquals(StudyMemberErrorCode.STUDY_MEMBER_FULL, exception.getErrorCode());

            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudyJoin(studyJoinId);
            verify(mockStudyJoin).isSameStudy(mockStudy);
            verify(mockStudyJoin, never()).approve();
            verify(studyMemberRepository, never()).save(any(StudyMember.class));
        }

        @Test
        @DisplayName("스터디가입이 요청한 스터디와 일치하지 않을 때 MosException 발생")
        void approveStudyMember_StudyJoinMismatch() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyJoinId = 1L;
            User mockUser = mock(User.class);
            Study mockStudy = mock(Study.class);
            StudyJoin mockStudyJoin = mock(StudyJoin.class);

            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getStudyJoin(studyJoinId)).thenReturn(mockStudyJoin);
            when(mockStudyJoin.isSameStudy(mockStudy)).thenReturn(false);

            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyJoinService.approveStudyJoin(userId, studyId, studyJoinId);
            });

            assertEquals(StudyJoinErrorCode.STUDY_JOIN_MISMATCH, exception.getErrorCode());

            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudyJoin(studyJoinId);
            verify(mockStudyJoin).isSameStudy(mockStudy);
            verify(mockStudyJoin, never()).approve();
            verify(studyMemberRepository, never()).save(any(StudyMember.class));
        }

    }

    @Nested
    @DisplayName("스터디 신청 거절 성공 시나리오")
    class RejectSuccessScenarios {
        @Test
        @DisplayName("스터디 신청 거절")
        void rejectStudyMember_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyJoinId = 1L;
            User mockUser = mock(User.class);
            Study mockStudy = mock(Study.class);
            StudyJoin mockStudyJoin = mock(StudyJoin.class);

            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getStudyJoin(studyJoinId)).thenReturn(mockStudyJoin);
            when(mockStudyJoin.isSameStudy(mockStudy)).thenReturn(true);

            // When
            studyJoinService.rejectStudyJoin(userId, studyId, studyJoinId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudyJoin(studyJoinId);
            verify(mockStudyJoin).isSameStudy(mockStudy);
            verify(mockStudyJoin).reject();
        }
    }

    @Nested
    @DisplayName("스터디 신청 거절 실패 시나리오")
    class RejectFailureScenarios {
        @Test
        @DisplayName("스터디가입이 요청한 스터디와 일치하지 않을 때 MosException 발생")
        void rejectStudyMember_StudyJoinMismatch() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyJoinId = 1L;
            User mockUser = mock(User.class);
            Study mockStudy = mock(Study.class);
            StudyJoin mockStudyJoin = mock(StudyJoin.class);

            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getStudyJoin(studyJoinId)).thenReturn(mockStudyJoin);
            when(mockStudyJoin.isSameStudy(mockStudy)).thenReturn(false);

            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyJoinService.rejectStudyJoin(userId, studyId, studyJoinId);
            });

            assertEquals(StudyJoinErrorCode.STUDY_JOIN_MISMATCH, exception.getErrorCode());

            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudyJoin(studyJoinId);
            verify(mockStudyJoin).isSameStudy(mockStudy);
            verify(mockStudyJoin, never()).reject();
        }
    }

    @Nested
    @DisplayName("스터디 신청 취소 성공 시나리오")
    class CancelSuccessScenarios {
        @Test
        @DisplayName("스터디 신청 취소")
        void cancelStudyMember_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyJoinId = 1L;
            User mockUser = mock(User.class);
            Study mockStudy = mock(Study.class);
            StudyJoin mockStudyJoin = mock(StudyJoin.class);

            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getStudyJoin(studyJoinId)).thenReturn(mockStudyJoin);
            when(mockStudyJoin.isPending()).thenReturn(true);
            when(mockStudyJoin.isSameStudy(mockStudy)).thenReturn(true);

            // When
            studyJoinService.cancelStudyJoin(userId, studyId, studyJoinId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudyJoin(studyJoinId);
            verify(mockStudyJoin).isSameStudy(mockStudy);
            verify(mockStudyJoin).cancel();
        }
    }

    @Nested
    @DisplayName("스터디 신청 취소 실패 시나리오")
    class CancelFailureScenarios {
        @Test
        @DisplayName("스터디가입이 요청한 스터디와 일치하지 않을 때 MosException 발생")
        void cancelStudyMember_StudyJoinMismatch() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyJoinId = 1L;
            User mockUser = mock(User.class);
            Study mockStudy = mock(Study.class);
            StudyJoin mockStudyJoin = mock(StudyJoin.class);

            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getStudyJoin(studyJoinId)).thenReturn(mockStudyJoin);
            when(mockStudyJoin.isSameStudy(mockStudy)).thenReturn(false);

            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyJoinService.cancelStudyJoin(userId, studyId, studyJoinId);
            });

            assertEquals(StudyJoinErrorCode.STUDY_JOIN_MISMATCH, exception.getErrorCode());

            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudyJoin(studyJoinId);
            verify(mockStudyJoin).isSameStudy(mockStudy);
            verify(mockStudyJoin, never()).cancel();
        }

        @Test
        @DisplayName("스터디 신청 상태가 PENDING이 아닐 때 MosException 발생")
        void deleteStudyJoin_NotPending() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyJoinId = 1L;
            User mockUser = mock(User.class);
            Study mockStudy = mock(Study.class);
            StudyJoin mockStudyJoin = mock(StudyJoin.class);

            when(entityFacade.getUser(userId)).thenReturn(mockUser);
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getStudyJoin(studyJoinId)).thenReturn(mockStudyJoin);
            when(mockStudyJoin.isSameStudy(mockStudy)).thenReturn(true);
            when(mockStudyJoin.isPending()).thenReturn(false);

            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyJoinService.cancelStudyJoin(userId, studyId, studyJoinId);
            });

            assertEquals(StudyJoinErrorCode.STUDY_JOIN_NOT_PENDING, exception.getErrorCode());

            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudyJoin(studyJoinId);
            verify(mockStudyJoin).isSameStudy(mockStudy);
            verify(mockStudyJoin).isPending();
            verify(mockStudyJoin, never()).cancel();
        }
    }
}
