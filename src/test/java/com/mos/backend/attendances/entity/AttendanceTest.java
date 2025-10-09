package com.mos.backend.attendances.entity;

import com.mos.backend.attendances.entity.exception.AttendanceErrorCode;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studyschedules.entity.StudySchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("Attendance 생성 시 상태 테스트")
class AttendanceTest {
    private static final StudySchedule STUDY_SCHEDULE = mock(StudySchedule.class);
    private static final StudyMember STUDY_MEMBER = mock(StudyMember.class);
    private static final Integer LATE_THRESHOLD = 10;
    private static final Integer ABSENCE_THRESHOLD = 20;

    @Nested
    @DisplayName("createWithThreshold 시나리오")
    class CreateWithThresholdScenario {
        @Nested
        @DisplayName("성공 시나리오")
        class SuccessScenario {
            @Test
            @DisplayName("정상 출석 상태 저장")
            void presentSuccess() {
                // given
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime start = now.minusMinutes(5);
                LocalDateTime end = now.plusMinutes(30);
                given(STUDY_SCHEDULE.getStartDateTime()).willReturn(start);
                given(STUDY_SCHEDULE.getEndDateTime()).willReturn(end);
                // when
                Attendance attendance = Attendance.createWithThreshold(STUDY_SCHEDULE, STUDY_MEMBER, LATE_THRESHOLD, ABSENCE_THRESHOLD);
                // then
                assertThat(attendance.getAttendanceStatus()).isEqualTo(AttendanceStatus.PRESENT);
            }

            @Test
            @DisplayName("지각 상태 저장")
            void lateSuccess() {
                // given
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime start = now.minusMinutes(LATE_THRESHOLD + 1);
                LocalDateTime end = now.plusMinutes(30);
                given(STUDY_SCHEDULE.getStartDateTime()).willReturn(start);
                given(STUDY_SCHEDULE.getEndDateTime()).willReturn(end);
                // when
                Attendance attendance = Attendance.createWithThreshold(STUDY_SCHEDULE, STUDY_MEMBER, LATE_THRESHOLD, ABSENCE_THRESHOLD);
                // then
                assertThat(attendance.getAttendanceStatus()).isEqualTo(AttendanceStatus.LATE);
            }

            @Test
            @DisplayName("무단 결석 상태 저장")
            void absenceSuccess() {
                // given
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime start = now.minusMinutes(60);
                LocalDateTime end = now.minusMinutes(ABSENCE_THRESHOLD + 1);
                given(STUDY_SCHEDULE.getStartDateTime()).willReturn(start);
                given(STUDY_SCHEDULE.getEndDateTime()).willReturn(end);
                // when
                Attendance attendance = Attendance.createWithThreshold(STUDY_SCHEDULE, STUDY_MEMBER, LATE_THRESHOLD, ABSENCE_THRESHOLD);
                // then
                assertThat(attendance.getAttendanceStatus()).isEqualTo(AttendanceStatus.UNEXCUSED_ABSENCE);
            }
        }

        @Nested
        @DisplayName("실패 시나리오")
        class FailScenario {
            @Test
            @DisplayName("출석 가능 시간이 아닌 경우 예외 발생")
            void notPresentTimeFail() {
                // given
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime start = now.plusMinutes(LATE_THRESHOLD + 1);
                LocalDateTime end = now.plusMinutes(30);
                given(STUDY_SCHEDULE.getStartDateTime()).willReturn(start);
                given(STUDY_SCHEDULE.getEndDateTime()).willReturn(end);
                // when
                MosException ex = assertThrows(
                        MosException.class, () -> Attendance.createWithThreshold(STUDY_SCHEDULE, STUDY_MEMBER, LATE_THRESHOLD, ABSENCE_THRESHOLD)
                );
                // then
                assertThat(ex.getErrorCode()).isEqualTo(AttendanceErrorCode.NOT_PRESENT_TIME);
            }
        }
    }
}
