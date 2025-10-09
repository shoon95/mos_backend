package com.mos.backend.studyschedules.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.application.StudyService;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import com.mos.backend.studycurriculum.infrastructure.StudyCurriculumRepository;
import com.mos.backend.studymembers.application.StudyMemberService;
import com.mos.backend.studyschedulecurriculums.application.StudyScheduleCurriculumService;
import com.mos.backend.studyschedulecurriculums.infrastructure.StudyScheduleCurriculumRepository;
import com.mos.backend.studyschedules.entity.StudySchedule;
import com.mos.backend.studyschedules.infrastructure.StudyScheduleRepository;
import com.mos.backend.studyschedules.infrastructure.dto.StudyScheduleWithAttendanceDto;
import com.mos.backend.studyschedules.presentation.req.StudyScheduleCreateReq;
import com.mos.backend.studyschedules.presentation.req.StudyScheduleUpdateReq;
import com.mos.backend.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudyScheduleService 테스트")
class StudyScheduleServiceTest {
    @InjectMocks
    private StudyScheduleService studyScheduleService;
    @Mock
    private EntityFacade entityFacade;
    @Mock
    private StudyService studyService;
    @Mock
    private StudyMemberService studyMemberService;
    @Mock
    private StudyScheduleCurriculumService studyScheduleCurriculumService;
    @Mock
    private StudyScheduleRepository studyScheduleRepository;
    @Mock
    private StudyCurriculumRepository studyCurriculumRepository;
    @Mock
    private StudyScheduleCurriculumRepository studyScheduleCurriculumRepository;

    @Nested
    @DisplayName("스터디 일정 생성 성공 시나리오")
    class CreateStudyScheduleSuccessScenario {
        @Test
        @DisplayName("스터디 일정 생성 성공")
        void createStudySchedule_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;

            User mockUser = mock(User.class);
            Study mockStudy = mock(Study.class);
            StudyScheduleCreateReq req = mock(StudyScheduleCreateReq.class);
            StudySchedule mockStudySchedule = mock(StudySchedule.class);

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyScheduleRepository.save(any())).thenReturn(mockStudySchedule);

            // When
            studyScheduleService.createStudySchedule(studyId, req);

            // Then
            verify(entityFacade).getStudy(studyId);
            verify(studyScheduleRepository).save(any(StudySchedule.class));
        }
    }

    @Nested
    @DisplayName("스터디 일정 생성 실패 시나리오")
    class CreateStudyScheduleFailScenario {
        @Test
        @DisplayName("일정 종료 시간이 시작 시간보다 이전인 경우 MosException 발생")
        void validateEndDateTime_Fail() {
            // Given
            Long studyId = 1L;
            Study mockStudy = mock(Study.class);
            StudyScheduleCreateReq req = mock(StudyScheduleCreateReq.class);

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(req.getStartDateTime()).thenReturn(java.time.LocalDateTime.of(2024, 6, 1, 10, 0));
            when(req.getEndDateTime()).thenReturn(java.time.LocalDateTime.of(2024, 6, 1, 9, 0));

            // When & Then
            MosException exception = assertThrows(MosException.class,
                    () -> studyScheduleService.createStudySchedule(studyId, req)
            );
            assertEquals(exception.getErrorCode(), com.mos.backend.studyschedules.entity.exception.StudyScheduleErrorCode.INVALID_END_DATE_TIME);
        }
    }

    @Nested
    @DisplayName("스터디 일정 조회 성공 시나리오")
    class GetStudyScheduleSuccessScenario {
        private static StudyScheduleWithAttendanceDto dto = mock(StudyScheduleWithAttendanceDto.class);
        private static List<StudyScheduleWithAttendanceDto> dtos = List.of(dto);
        private static final Long STUDY_SCHEDULE_ID = 1L;

        @BeforeEach
        void setUp() {
            when(dto.getStudyScheduleId()).thenReturn(STUDY_SCHEDULE_ID);
        }

        @Test
        @DisplayName("내 스터디 일정 조회 성공")
        void getMyStudySchedules_Success() {
            // Given
            Long userId = 1L;
            User user = mock(User.class);
            StudySchedule studySchedule = mock(StudySchedule.class);
            List<StudySchedule> studySchedules = List.of(studySchedule);
            List<StudyCurriculum> studyCurriculums = List.of(mock(StudyCurriculum.class));

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(user.getId()).thenReturn(userId);
            when(studyScheduleRepository.findAllByActivatedUserId(userId)).thenReturn(dtos);
            when(studyCurriculumRepository.findAllByStudyScheduleId(STUDY_SCHEDULE_ID)).thenReturn(studyCurriculums);

            // When
            studyScheduleService.getMyStudySchedules(userId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(studyScheduleRepository).findAllByActivatedUserId(userId);
            verify(studyCurriculumRepository, times(studySchedules.size())).findAllByStudyScheduleId(STUDY_SCHEDULE_ID);
        }

        @Test
        @DisplayName("스터디 일정 조회 성공")
        void getStudySchedules_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);
            List<StudySchedule> studySchedules = List.of(studySchedule);
            List<StudyCurriculum> studyCurriculums = List.of(mock(StudyCurriculum.class));

            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(study.getId()).thenReturn(studyId);
            when(studyScheduleRepository.findByStudyIdWithAttendance(userId, studyId)).thenReturn(dtos);
            when(studyCurriculumRepository.findAllByStudyScheduleId(STUDY_SCHEDULE_ID)).thenReturn(studyCurriculums);

            // When
            studyScheduleService.getStudySchedules(userId, studyId);

            // Then
            verify(entityFacade).getStudy(studyId);
            verify(studyScheduleRepository).findByStudyIdWithAttendance(userId, studyId);
            verify(studyCurriculumRepository, times(studySchedules.size())).findAllByStudyScheduleId(anyLong());
        }
    }

    @Nested
    @DisplayName("스터디 일정 수정 성공 시나리오")
    class UpdateStudyScheduleSuccessScenario {
        @Test
        @DisplayName("스터디 일정 수정 성공")
        void updateStudySchedule_Success() {
            // Given
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);

            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);
            when(study.getId()).thenReturn(studyId);

            // When
            studyScheduleService.updateStudySchedule(studyId, studyScheduleId, mock(StudyScheduleUpdateReq.class));

            // Then
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudySchedule(studyScheduleId);
            verify(studySchedule).update(any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("스터디 일정 수정 실패 시나리오")
    class UpdateStudyScheduleFailScenario {
        @Test
        @DisplayName("일정 종료 시간이 시작 시간보다 이전인 경우 MosException 발생")
        void validateEndDateTime_Fail() {
            // Given
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            Study mockStudy = mock(Study.class);
            StudySchedule mockStudySchedule = mock(StudySchedule.class);
            StudyScheduleUpdateReq req = mock(StudyScheduleUpdateReq.class);

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(mockStudySchedule);
            when(req.getStartDateTime()).thenReturn(java.time.LocalDateTime.of(2024, 6, 1, 10, 0));
            when(req.getEndDateTime()).thenReturn(java.time.LocalDateTime.of(2024, 6, 1, 9, 0));

            // When & Then
            MosException exception = assertThrows(MosException.class,
                    () -> studyScheduleService.updateStudySchedule(studyId, studyScheduleId, req)
            );
            assertEquals(exception.getErrorCode(), com.mos.backend.studyschedules.entity.exception.StudyScheduleErrorCode.INVALID_END_DATE_TIME);

            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudySchedule(studyScheduleId);
            verify(mockStudySchedule, never()).update(any(), any(), any(), any());
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

            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);

            // When
            studyScheduleService.deleteStudySchedule(studyId, studyScheduleId);

            // Then
            verify(entityFacade).getStudySchedule(studyScheduleId);
            verify(studyScheduleRepository).delete(studySchedule);
        }
    }
}
