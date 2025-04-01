package com.mos.backend.attendances.application;

import com.mos.backend.attendances.entity.Attendance;
import com.mos.backend.attendances.entity.AttendanceStatus;
import com.mos.backend.attendances.entity.exception.AttendanceErrorCode;
import com.mos.backend.attendances.infrastructure.AttendanceRepository;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studymembers.infrastructure.StudyMemberRepository;
import com.mos.backend.studyschedules.entity.StudySchedule;
import com.mos.backend.studyschedules.entity.exception.StudyScheduleErrorCode;
import com.mos.backend.studyschedules.infrastructure.StudyScheduleRepository;
import com.mos.backend.users.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AttendanceService 테스트")
class AttendanceServiceTest {

    @Mock
    private EntityFacade entityFacade;
    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private StudyMemberRepository studyMemberRepository;
    @Mock
    private StudyScheduleRepository studyScheduleRepository;
    @InjectMocks
    private AttendanceService attendanceService;

    @Nested
    @DisplayName("출석 생성 성공 시나리오")
    class CreateAttendanceSuccessScenario {
        @Test
        @DisplayName("출석 생성 성공")
        void createAttendance_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            User user = mock(User.class);
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);
            StudyMember studyMember = mock(StudyMember.class);

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);
            when(studyMemberRepository.findByUserIdAndStudyId(userId, studyId)).thenReturn(Optional.of(studyMember));
            when(studySchedule.isBeforePresentTime()).thenReturn(false);
            when(study.getId()).thenReturn(studyId);
            when(user.getId()).thenReturn(userId);
            when(studySchedule.getStudy()).thenReturn(study);
            when(study.isRelated(studyId)).thenReturn(true);
            when(studySchedule.isPresentTime()).thenReturn(true);

            // When
            attendanceService.create(userId, studyId, studyScheduleId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudySchedule(studyScheduleId);
            verify(studyMemberRepository).findByUserIdAndStudyId(userId, studyId);
            verify(attendanceRepository).save(any());
        }

        @Test
        @DisplayName("지각 생성 성공")
        void createLateAttendance_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            User user = mock(User.class);
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);
            StudyMember studyMember = mock(StudyMember.class);

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);
            when(studyMemberRepository.findByUserIdAndStudyId(userId, studyId)).thenReturn(Optional.of(studyMember));
            when(study.getId()).thenReturn(studyId);
            when(user.getId()).thenReturn(userId);
            when(studySchedule.getStudy()).thenReturn(study);
            when(study.isRelated(studyId)).thenReturn(true);
            when(studySchedule.isPresentTime()).thenReturn(false);

            // When
            attendanceService.create(userId, studyId, studyScheduleId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudySchedule(studyScheduleId);
            verify(studyMemberRepository).findByUserIdAndStudyId(userId, studyId);
            verify(attendanceRepository).save(any());
        }
    }

    @Nested
    @DisplayName("출석 생성 실패 시나리오")
    class CreateAttendanceFailScenario {
        @Test
        @DisplayName("스터디 일정의 시작 시간 이전에 출석을 시도한 경우 MosException 발생")
        void createAttendance_Fail_BeforePresentTime() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            User user = mock(User.class);
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);
            when(user.getId()).thenReturn(userId);
            when(study.getId()).thenReturn(studyId);
            when(studyMemberRepository.findByUserIdAndStudyId(userId, studyId)).thenReturn(Optional.of(mock(StudyMember.class)));
            when(studySchedule.getStudy()).thenReturn(study);
            when(study.isRelated(studyId)).thenReturn(true);
            when(studySchedule.isBeforePresentTime()).thenReturn(true);

            // When
            MosException exception = assertThrows(MosException.class, () -> attendanceService.create(userId, studyId, studyScheduleId));

            // Then
            assertEquals(exception.getErrorCode(), AttendanceErrorCode.NOT_PRESENT_TIME);
        }

        @Test
        @DisplayName("스터디 스케줄이 완료된 경우 MosException 발생")
        void createAttendance_Fail_StudyScheduleCompleted() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            User user = mock(User.class);
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);
            when(studySchedule.getStudy()).thenReturn(study);
            when(study.getId()).thenReturn(studyId);
            when(study.isRelated(studyId)).thenReturn(true);
            when(studySchedule.isCompleted()).thenReturn(true);

            // When
            MosException exception = assertThrows(MosException.class, () -> attendanceService.create(userId, studyId, studyScheduleId));

            // Then
            assertEquals(exception.getErrorCode(), StudyScheduleErrorCode.STUDY_SCHEDULE_COMPLETED);
        }

        @Test
        @DisplayName("이미 출석한 경우 MosException 발생")
        void createAttendance_Fail_AttendanceAlreadyExists() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            User user = mock(User.class);
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);
            StudyMember studyMember = mock(StudyMember.class);
            Optional<Attendance> OptionalAttendance = Optional.of(mock(Attendance.class));

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);
            when(studyMemberRepository.findByUserIdAndStudyId(userId, studyId)).thenReturn(Optional.of(studyMember));
            when(study.getId()).thenReturn(studyId);
            when(user.getId()).thenReturn(userId);
            when(studySchedule.getStudy()).thenReturn(study);
            when(study.isRelated(studyId)).thenReturn(true);
            when(attendanceRepository.findByStudyScheduleAndStudyMember(studySchedule, studyMember)).thenReturn(OptionalAttendance);

            // When
            MosException exception = assertThrows(MosException.class, () -> attendanceService.create(userId, studyId, studyScheduleId));

            // Then
            verify(attendanceRepository).findByStudyScheduleAndStudyMember(studySchedule, studyMember);
            assertEquals(exception.getErrorCode(), AttendanceErrorCode.ATTENDANCE_ALREADY_EXISTS);
        }
    }

    @Nested
    @DisplayName("출석 수정 성공 시나리오")
    class UpdateAttendanceSuccessScenario {
        @Test
        @DisplayName("출석 수정 성공")
        void updateAttendance_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            User user = mock(User.class);
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);
            StudyMember studyMember = mock(StudyMember.class);
            Attendance attendance = mock(Attendance.class);
            Optional<Attendance> optionalAttendance = Optional.of(attendance);
            AttendanceStatus modifiableAttendanceStatus = AttendanceStatus.EARLY_LEAVE;

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);
            when(studyMemberRepository.findByUserIdAndStudyId(userId, studyId)).thenReturn(Optional.of(studyMember));
            when(study.getId()).thenReturn(studyId);
            when(user.getId()).thenReturn(userId);
            when(studySchedule.getStudy()).thenReturn(study);
            when(study.isRelated(studyId)).thenReturn(true);
            when(attendanceRepository.findByStudyScheduleAndStudyMember(studySchedule, studyMember)).thenReturn(optionalAttendance);

            // When
            attendanceService.update(userId, studyId, studyScheduleId, modifiableAttendanceStatus.getDescription());

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudySchedule(studyScheduleId);
            verify(studyMemberRepository).findByUserIdAndStudyId(userId, studyId);
            verify(attendanceRepository).findByStudyScheduleAndStudyMember(studySchedule, studyMember);
            verify(attendance).updateStatus(any());
        }
    }

    @Nested
    @DisplayName("출석 수정 실패 시나리오")
    class UpdateAttendanceFailScenario {
        @Test
        @DisplayName("출석 수정 실패")
        void updateAttendance_Fail() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            User user = mock(User.class);
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);
            StudyMember studyMember = mock(StudyMember.class);
            Attendance attendance = mock(Attendance.class);
            Optional<Attendance> optionalAttendance = Optional.of(attendance);
            AttendanceStatus unModifiableAttendanceStatus = AttendanceStatus.EARLY_LEAVE;

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);
            when(studyMemberRepository.findByUserIdAndStudyId(userId, studyId)).thenReturn(Optional.of(studyMember));
            when(study.getId()).thenReturn(studyId);
            when(user.getId()).thenReturn(userId);
            when(studySchedule.getStudy()).thenReturn(study);
            when(study.isRelated(studyId)).thenReturn(true);
            when(attendanceRepository.findByStudyScheduleAndStudyMember(studySchedule, studyMember)).thenReturn(optionalAttendance);

            // When
            attendanceService.update(userId, studyId, studyScheduleId, unModifiableAttendanceStatus.getDescription());

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudySchedule(studyScheduleId);
            verify(studyMemberRepository).findByUserIdAndStudyId(userId, studyId);
            verify(attendanceRepository).findByStudyScheduleAndStudyMember(studySchedule, studyMember);
            verify(attendance).updateStatus(any());
        }
    }

    @Nested
    @DisplayName("팀원의 스터디 출석률 조회 성공 시나리오")
    class GetStudyMemberAttendanceRateSuccessScenario {
        @Test
        @DisplayName("팀원의 스터디 출석률 조회 성공")
        void getStudyMemberAttendanceRate_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyMemberId = 1L;
            Long studyScheduleId = 1L;
            User user = mock(User.class);
            Study study = mock(Study.class);
            StudyMember studyMember = mock(StudyMember.class);
            List<StudyMember> studyMembers = List.of(studyMember);
            Attendance attendance = mock(Attendance.class);
            StudySchedule studySchedule = mock(StudySchedule.class);
            List<Attendance> attendances = List.of(attendance);

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(study.getId()).thenReturn(studyId);
            when(user.getId()).thenReturn(userId);
            when(studyMember.getUser()).thenReturn(user);
            when(studyMemberRepository.findAllByStudyId(studyId)).thenReturn(studyMembers);
            when(studyMember.getId()).thenReturn(studyMemberId);
            when(attendanceRepository.findAllByStudyMemberId(studyMemberId)).thenReturn(attendances);
            when(attendance.getStudySchedule()).thenReturn(studySchedule);
            when(studySchedule.getId()).thenReturn(studyScheduleId);
            when(attendance.isAttended()).thenReturn(true);
            when(attendance.getAttendanceStatus()).thenReturn(AttendanceStatus.PRESENT);

            // When
            attendanceService.getStudyMemberAttendances(userId, studyId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(studyMemberRepository).findAllByStudyId(studyId);
            verify(attendanceRepository).findAllByStudyMemberId(studyMemberId);
            verify(studyMemberRepository).findAllByStudyId(studyId);
            verify(attendanceRepository).findAllByStudyMemberId(studyMemberId);
            verify(attendance).isAttended();
        }
    }

    @Nested
    @DisplayName("스터디 출석률 조회 성공 시나리오")
    class GetStudyAttendanceRateSuccessScenario {
        @Test
        @DisplayName("스터디 출석률 조회 성공")
        void getStudyAttendanceRate_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            User user = mock(User.class);
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);
            List<StudySchedule> studySchedules = List.of(studySchedule);
            Attendance attendance = mock(Attendance.class);
            List<Attendance> attendances = List.of(attendance);

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(study.getId()).thenReturn(studyId);
            when(studyScheduleRepository.findByStudyId(studyId)).thenReturn(studySchedules);
            when(attendanceRepository.findAllByStudyScheduleId(studyScheduleId)).thenReturn(attendances);
            when(attendance.isAttended()).thenReturn(true);
            when(studySchedule.getId()).thenReturn(studyScheduleId);

            // When
            attendanceService.getStudyScheduleAttendanceRates(userId, studyId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(studyScheduleRepository).findByStudyId(studyId);
            verify(attendanceRepository).findAllByStudyScheduleId(studyScheduleId);
        }
    }

    @Nested
    @DisplayName("출석 삭제 성공 시나리오")
    class DeleteAttendanceSuccessScenario {
        @Test
        @DisplayName("출석 삭제 성공")
        void deleteAttendance_Success() {
            // Given
            Long userId = 1L;
            Long studyId = 1L;
            Long studyScheduleId = 1L;
            Long studyMemberId = 1L;
            Long attendanceId = 1L;
            User user = mock(User.class);
            Study study = mock(Study.class);
            StudySchedule studySchedule = mock(StudySchedule.class);
            StudyMember studyMember = mock(StudyMember.class);
            Attendance attendance = mock(Attendance.class);

            when(entityFacade.getUser(userId)).thenReturn(user);
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getStudySchedule(studyScheduleId)).thenReturn(studySchedule);
            when(entityFacade.getAttendance(attendanceId)).thenReturn(attendance);
            when(studyMemberRepository.findByUserIdAndStudyId(userId, studyId)).thenReturn(Optional.of(studyMember));
            when(study.getId()).thenReturn(studyId);
            when(user.getId()).thenReturn(userId);
            when(studySchedule.getStudy()).thenReturn(study);
            when(study.isRelated(studyId)).thenReturn(true);
            when(studySchedule.getId()).thenReturn(studyScheduleId);
            when(studyMember.getId()).thenReturn(studyMemberId);
            when(attendance.isRelated(studyScheduleId, studyMemberId)).thenReturn(true);

            // When
            attendanceService.delete(userId, studyId, studyScheduleId, attendanceId);

            // Then
            verify(entityFacade).getUser(userId);
            verify(entityFacade).getStudy(studyId);
            verify(entityFacade).getStudySchedule(studyScheduleId);
            verify(entityFacade).getAttendance(attendanceId);
            verify(studyMemberRepository).findByUserIdAndStudyId(userId, studyId);
            verify(attendanceRepository).delete(attendance);
        }
    }
}
