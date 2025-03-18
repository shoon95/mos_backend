package com.mos.backend.studyschedules.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import com.mos.backend.studyschedules.entity.StudySchedule;
import com.mos.backend.studyschedules.infrastructure.StudyScheduleRepository;
import com.mos.backend.studyschedules.presentation.req.CurriculumScheduleCreateReq;
import com.mos.backend.studyschedules.presentation.req.StudyScheduleCreateReq;
import com.mos.backend.studyschedules.presentation.req.StudyScheduleUpdateReq;
import com.mos.backend.users.entity.User;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudyScheduleService 테스트")
class StudyScheduleServiceTest {
    @Mock
    private StudyScheduleRepository studyScheduleRepository;
    @Mock
    private EntityFacade entityFacade;
    @InjectMocks
    private StudyScheduleService studyScheduleService;

    @Nested
    @DisplayName("스터디 일정 생성 성공 시나리오")
    class CreateStudyScheduleSuccessScenario {
        @Test
        @DisplayName("스터디 일정 생성 성공")
        void createStudySchedule_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;

            when(entityFacade.getUser(userId)).thenReturn(mock(User.class));
            when(entityFacade.getStudy(studyId)).thenReturn(mock(Study.class));

            // When
            studyScheduleService.createStudySchedule(userId, studyId, mock(StudyScheduleCreateReq.class));

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(studyScheduleRepository).save(any(StudySchedule.class));
        }

        @Test
        @DisplayName("커리큘럼으로 스터디 일정 생성 성공")
        void createStudyScheduleByCurriculum_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyCurriculumId = 1L;
            Study study = mock(Study.class);
            StudyCurriculum studyCurriculum = mock(StudyCurriculum.class);

            when(entityFacade.getUser(userId)).thenReturn(mock(User.class));
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudyCurriculum(studyCurriculumId)).thenReturn(studyCurriculum);
            when(studyCurriculum.getStudy()).thenReturn(study);
            when(study.getId()).thenReturn(studyId);
            when(study.isRelational(studyId)).thenReturn(true);

            // When
            studyScheduleService.createStudyScheduleByCurriculum(userId, studyId, studyCurriculumId, mock(CurriculumScheduleCreateReq.class));

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudyCurriculum(studyCurriculumId);
            verify(studyScheduleRepository).save(any(StudySchedule.class));
        }
    }

    @Nested
    @DisplayName("스터디 일정 생성 실패 시나리오")
    class CreateStudyScheduleFailScenario {
        @Test
        @DisplayName("스터디 커리큘럼이 스터디와 일치하지 않는 경우 MosException 발생")
        void createStudyScheduleByCurriculum_Fail() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyCurriculumId = 1L;
            Study study = mock(Study.class);
            StudyCurriculum studyCurriculum = mock(StudyCurriculum.class);

            when(entityFacade.getUser(userId)).thenReturn(mock(User.class));
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudyCurriculum(studyCurriculumId)).thenReturn(studyCurriculum);
            when(studyCurriculum.getStudy()).thenReturn(study);
            when(study.getId()).thenReturn(studyId);
            when(study.isRelational(studyId)).thenReturn(false);

            // When & Then
            MosException exception = assertThrows(MosException.class,
                    () -> studyScheduleService.createStudyScheduleByCurriculum(userId, studyId, studyCurriculumId, mock(CurriculumScheduleCreateReq.class))
            );

            assertEquals(exception.getErrorCode(), StudyErrorCode.UNRELATED_STUDY);
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudyCurriculum(studyCurriculumId);
            verify(studyScheduleRepository, never()).save(any(StudySchedule.class));
        }
    }

    @Nested
    @DisplayName("스터디 일정 조회 성공 시나리오")
    class GetStudyScheduleSuccessScenario {
        @Test
        @DisplayName("내 스터디 일정 조회 성공")
        void getMyStudySchedules_Success() {
            // Given
            Long userId = 1L;
            User user = mock(User.class);

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(user.getId()).thenReturn(userId);
            when(studyScheduleRepository.findAllByActivatedUserId(userId)).thenReturn(mock(List.class));

            // When
            studyScheduleService.getMyStudySchedules(userId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(studyScheduleRepository).findAllByActivatedUserId(userId);
        }

        @Test
        @DisplayName("스터디 일정 조회 성공")
        void getStudySchedules_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Study study = mock(Study.class);

            when(entityFacade.getUser(userId)).thenReturn(mock(User.class));
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(study.getId()).thenReturn(studyId);
            when(studyScheduleRepository.findByStudyId(studyId)).thenReturn(mock(List.class));

            // When
            studyScheduleService.getStudySchedules(userId, studyId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(studyScheduleRepository).findByStudyId(studyId);
        }
    }

    @Nested
    @DisplayName("스터디 일정 수정 성공 시나리오")
    class UpdateStudyScheduleSuccessScenario {
        @Test
        @DisplayName("스터디 일정 수정 성공")
        void updateStudySchedule_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);

            when(entityFacade.getUser(userId)).thenReturn(mock(User.class));
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);
            when(studySchedule.getStudy()).thenReturn(study);
            when(study.getId()).thenReturn(studyId);
            when(study.isRelational(studyId)).thenReturn(true);

            // When
            studyScheduleService.updateStudySchedule(userId, studyId, studyScheduleId, mock(StudyScheduleUpdateReq.class));

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudySchedule(studyScheduleId);
            verify(studySchedule).update(any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("스터디 일정 수정 실패 시나리오")
    class UpdateStudyScheduleFailScenario {
        @Test
        @DisplayName("스터디 일정 수정 실패 - 스터디와 스터디 일정이 일치하지 않는 경우 MosException 발생")
        void updateStudySchedule_Fail() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);

            when(entityFacade.getUser(userId)).thenReturn(mock(User.class));
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);
            when(studySchedule.getStudy()).thenReturn(study);
            when(study.getId()).thenReturn(studyId);
            when(study.isRelational(studyId)).thenReturn(false);

            // When & Then
            MosException exception = assertThrows(MosException.class,
                    () -> studyScheduleService.updateStudySchedule(userId, studyId, studyScheduleId, mock(StudyScheduleUpdateReq.class))
            );

            assertEquals(exception.getErrorCode(), StudyErrorCode.UNRELATED_STUDY);
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudySchedule(studyScheduleId);
            verify(studySchedule, never()).update(any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("스터디 일정 삭제 성공 시나리오")
    class DeleteStudyScheduleSuccessScenario {
        @Test
        @DisplayName("스터디 일정 삭제 성공")
        void deleteStudySchedule_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);

            when(entityFacade.getUser(userId)).thenReturn(mock(User.class));
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);
            when(studySchedule.getStudy()).thenReturn(study);
            when(study.getId()).thenReturn(studyId);
            when(study.isRelational(studyId)).thenReturn(true);

            // When
            studyScheduleService.deleteStudySchedule(userId, studyId, studyScheduleId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudySchedule(studyScheduleId);
            verify(studyScheduleRepository).delete(studySchedule);
        }
    }

    @Nested
    @DisplayName("스터디 일정 삭제 실패 시나리오")
    class DeleteStudyScheduleFailScenario {
        @Test
        @DisplayName("스터디 일정 삭제 실패 - 스터디와 스터디 일정이 일치하지 않는 경우 MosException 발생")
        void deleteStudySchedule_Fail() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);

            when(entityFacade.getUser(userId)).thenReturn(mock(User.class));
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);
            when(studySchedule.getStudy()).thenReturn(study);
            when(study.getId()).thenReturn(studyId);
            when(study.isRelational(studyId)).thenReturn(false);

            // When & Then
            MosException exception = assertThrows(MosException.class,
                    () -> studyScheduleService.deleteStudySchedule(userId, studyId, studyScheduleId)
            );

            assertEquals(exception.getErrorCode(), StudyErrorCode.UNRELATED_STUDY);
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudySchedule(studyScheduleId);
            verify(studyScheduleRepository, never()).delete(studySchedule);
        }
    }
}
