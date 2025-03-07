package com.mos.backend.studybenefits.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studybenefits.infrastructure.StudyBenefitRepository;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudyBenefitService 테스트")
class StudyBenefitServiceTest {
    @Mock
    private StudyBenefitRepository studyBenefitRepository;

    @Mock
    private StudyRepository studyRepository;

    @InjectMocks
    private StudyBenefitService studyBenefitService;

    @Nested
    @DisplayName("스터디 혜택 생성 성공 시나리오")
    class SuccessScenarios {
        @Test
        @DisplayName("정상적인 스터디 혜택 생성")
        void createStudyBenefit_Success() {
            // Given
            Long studyId = 1L;
            List<String> contents = List.of("혜택 1", "혜택 2", "혜택 3");
            Study mockStudy = Study.builder().id(studyId).build();

            when(studyRepository.findById(studyId)).thenReturn(Optional.of(mockStudy));

            // When
            studyBenefitService.create(studyId, contents);

            // Then
            verify(studyRepository).findById(studyId);
            verify(studyBenefitRepository).saveAll(anyList());
        }

        @Test
        @DisplayName("스터디 혜택이 비어있으면 아무일도 일어나지 않음")
        void createStudyBenefit_EmptyContents() {
            // Given
            Long studyId = 1L;
            List<String> contents = List.of();

            // When
            studyBenefitService.create(studyId, contents);

            // Then
            verify(studyRepository, never()).findById(anyLong());
            verify(studyBenefitRepository, never()).saveAll(anyList());
        }
    }

    @Nested
    @DisplayName("스터디 혜택 생성 실패 시나리오")
    class FailureScenarios {
        @Test
        @DisplayName("스터디가 존재하지 않을 때 MosException 발생")
        void createStudyBenefit_StudyNotFound() {
            // Given
            Long studyId = 1L;
            List<String> contents = List.of("혜택 1", "혜택 2");

            when(studyRepository.findById(studyId)).thenReturn(Optional.empty());

            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyBenefitService.create(studyId, contents);
            });

            assertEquals(StudyErrorCode.STUDY_NOT_FOUND, exception.getErrorCode());
            verify(studyRepository).findById(studyId);
            verify(studyBenefitRepository, never()).saveAll(anyList());
        }
    }
}