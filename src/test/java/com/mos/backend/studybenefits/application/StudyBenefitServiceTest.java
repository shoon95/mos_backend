package com.mos.backend.studybenefits.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studybenefits.application.responsedto.StudyBenefitResponseDto;
import com.mos.backend.studybenefits.entity.StudyBenefit;
import com.mos.backend.studybenefits.entity.exception.StudyBenefitErrorCode;
import com.mos.backend.studybenefits.infrastructure.StudyBenefitRepository;
import com.mos.backend.studybenefits.presentation.requestdto.StudyBenefitRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudyBenefitService 테스트")
class StudyBenefitServiceTest {
    @Mock
    private StudyBenefitRepository studyBenefitRepository;

    @Mock
    private EntityFacade entityFacade;

    @InjectMocks
    private StudyBenefitService studyBenefitService;

    @Nested
    @DisplayName("스터디 혜택 생성, 수정, 삭제 성공 시나리오")
    class CreateOrUpdateOrDeleteTest_Success {

        @Test
        @DisplayName("새로운 스터디 혜택 생성")
        void createStudyBenefit_Success() {
            // Given
            Long studyId = 1L;
            List<StudyBenefitRequestDto> contents = List.of(
                    createStudyBenefitRequestDto(null, 1L, "혜택 1"),
                    createStudyBenefitRequestDto(null, 2L, "혜택 2")
            );
            Study mockStudy = Study.builder().id(studyId).build();
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyBenefitRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>());
            // When
            studyBenefitService.createOrUpdateOrDelete(studyId, contents);

            // Then
            verify(studyBenefitRepository, times(2)).save(any(StudyBenefit.class));
        }
        @Test
        @DisplayName("기존 스터디 혜택 수정")
        void updateStudyBenefit_Success() {
            // Given
            Long studyId = 1L;
            Study mockStudy = Study.builder().id(studyId).build();

            StudyBenefitRequestDto benefitRequest = new StudyBenefitRequestDto();
            benefitRequest.setId(1L);
            benefitRequest.setBenefitNum(1L);
            benefitRequest.setContent("123");

            StudyBenefit mockStudyBenefit = mock(StudyBenefit.class);
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyBenefitRepository.findByIdAndStudy(studyId, mockStudy)).thenReturn(Optional.of(mockStudyBenefit));

            // when
            studyBenefitService.createOrUpdateOrDelete(studyId, List.of(benefitRequest));

            verify(mockStudyBenefit).changeNumAndContent(benefitRequest.getBenefitNum(), benefitRequest.getContent());
        }
        @Test
        @DisplayName("기존 스터디 혜택 삭제")
        void deleteStudyBenefit_Success() {
            // Given
            Long studyId = 1L;
            Study study = Study.builder().id(studyId).build();

            List<StudyBenefitRequestDto> contents = List.of(
                    createStudyBenefitRequestDto(1L, 1L, "혜택 1")
            );

            StudyBenefit studyBenefit1 = spy(StudyBenefit.create(study, 1L, "혜택1"));
            doReturn(1L).when(studyBenefit1).getId();
            StudyBenefit studyBenefit2 = spy(StudyBenefit.create(study, 2L, "혜택2"));
            doReturn(2L).when(studyBenefit2).getId();

            when(studyBenefitRepository.findAllByStudy(study)).thenReturn(new ArrayList<>(List.of(studyBenefit1, studyBenefit2)));
            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(studyBenefitRepository.findByIdAndStudy(eq(1L), eq(study))).thenReturn(Optional.of(studyBenefit1));

            // When
            studyBenefitService.createOrUpdateOrDelete(studyId, contents);

            // Then
            verify(studyBenefitRepository).deleteAll(List.of(studyBenefit2));
        }
    }

    @Nested
    @DisplayName("스터디 혜택 생성, 수정, 삭제 실패 시나리오")
    class createOrUpdateOrDeleteTest_Fail {

        @Test
        @DisplayName("benefitNum 순서가 빈 값이 존재하면 에러를 발생시킨다.")
        void nullBenefitNumCreateOrUpdateOrDelete_ThrowsException() {
            // Given
            Long studyId = 1L;
            List<StudyBenefitRequestDto> contents = List.of(
                    createStudyBenefitRequestDto(null, 1L, "혜택 1"),
                    createStudyBenefitRequestDto(null, null, "혜택 2")
            );

            // when
            MosException mosException = assertThrows(MosException.class, () -> studyBenefitService.createOrUpdateOrDelete(studyId, contents));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyBenefitErrorCode.INVALID_BENEFIT_NUM);
        }

        @Test
        @DisplayName("benefitNum 순서가 연속된 수가 아니라면에러를 발생시킨다.")
        void notContinuousBenefitNumCreateOrUpdateOrDelete_ThrowsException() {
            // given
            Long studyId = 1L;
            List<StudyBenefitRequestDto> contents = List.of(
                    createStudyBenefitRequestDto(null, 1L, "혜택 1"),
                    createStudyBenefitRequestDto(null, 3L, "혜택 2")
            );
            // when
            MosException mosException = assertThrows(MosException.class, () -> studyBenefitService.createOrUpdateOrDelete(studyId, contents));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyBenefitErrorCode.INVALID_BENEFIT_NUM);
        }

        @Test
        @DisplayName("benefitNum 순서가 1부터 시작되지 않으면 에러를 발생시킨다.")
        void benefitNumNotStartFrom1CreateOrUpdateOrDelete_ThrowsException() {
            // given
            Long studyId = 1L;
            List<StudyBenefitRequestDto> contents = List.of(
                    createStudyBenefitRequestDto(null, 2L, "혜택 1"),
                    createStudyBenefitRequestDto(null, 3L, "혜택 2")
            );

            // when
            MosException mosException = assertThrows(MosException.class, () -> studyBenefitService.createOrUpdateOrDelete(studyId, contents));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyBenefitErrorCode.INVALID_BENEFIT_NUM);
        }

        @Test
        @DisplayName("benefitNum 순서가 중복이면 에러를 발생시킨다.")
        void duplicatedBenefitNumCreateOrUpdateOrDelete_ThrowsException() {
            // given
            Long studyId = 1L;
            List<StudyBenefitRequestDto> contents = List.of(
                    createStudyBenefitRequestDto(null, 1L, "혜택 1"),
                    createStudyBenefitRequestDto(null, 1L, "혜택 2")
            );


            // when
            MosException mosException = assertThrows(MosException.class, () -> studyBenefitService.createOrUpdateOrDelete(studyId, contents));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyBenefitErrorCode.INVALID_BENEFIT_NUM);
        }

        @Test
        @DisplayName("스터디가 존재하지 않을 때 MosException 발생")
        void createStudyBenefit_StudyNotFound() {
            // Given
            Long studyId = 1L;
            List<StudyBenefitRequestDto> contents = List.of(
                    createStudyBenefitRequestDto(null, 1L, "혜택 1"),
                    createStudyBenefitRequestDto(null, 2L, "혜택 2")
            );
            when(entityFacade.getStudy(studyId)).thenThrow(new MosException(StudyBenefitErrorCode.STUDY_BENEFIT_NOT_FOUND));

            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyBenefitService.createOrUpdateOrDelete(studyId, contents);
            });

            assertThat(exception.getErrorCode()).isEqualTo(StudyBenefitErrorCode.STUDY_BENEFIT_NOT_FOUND);
            verify(entityFacade).getStudy(studyId);
            verify(studyBenefitRepository, never()).saveAll(anyList());
        }


        @Test
        @DisplayName("찾을 수 없는 studyBenefit")
        void findStudyBenefit_invalidStudyBenefitId() {
            // Given
            Long studyId = 1L;
            Long studyBenefitId = 1L;
            List<StudyBenefitRequestDto> contents = List.of(
                    createStudyBenefitRequestDto(studyBenefitId, 1L, "혜택 1")
            );
            Study mockStudy = Study.builder().id(studyId).build();
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyBenefitRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>());
            when(studyBenefitRepository.findByIdAndStudy(studyBenefitId, mockStudy)).thenReturn(Optional.empty());
            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyBenefitService.createOrUpdateOrDelete(studyId, contents);
            });

            assertThat(exception.getErrorCode()).isEqualTo(StudyBenefitErrorCode.STUDY_BENEFIT_NOT_FOUND);
            verify(entityFacade).getStudy(studyId);
            verify(studyBenefitRepository).findAllByStudy(mockStudy);
        }
    }

    @Test
    @DisplayName("studyBenefit 목록 조회 성공")
    void getAllStudyBenefits_Success() {
        Long studyId = 1L;
        Study study = Study.builder().id(studyId).build();
        StudyBenefit studyBenefit1 = spy(StudyBenefit.create(study, 2L, "1"));
        StudyBenefit studyBenefit2 = spy(StudyBenefit.create(study, 1L, "1"));
        StudyBenefit studyBenefit3 = spy(StudyBenefit.create(study, 4L, "1"));
        StudyBenefit studyBenefit4 = spy(StudyBenefit.create(study, 3L, "1"));

        when(entityFacade.getStudy(studyId)).thenReturn(study);
        when(studyBenefitRepository.findAllByStudy(study)).thenReturn(new ArrayList<>(List.of(studyBenefit1, studyBenefit2, studyBenefit3, studyBenefit4)));

        // when
        List<StudyBenefitResponseDto> all = studyBenefitService.getAll(studyId);

        // then
        int num = 1;
        for (StudyBenefitResponseDto studyBenefitResponseDto : all) {
            assertThat(studyBenefitResponseDto.getBenefitNum()).isEqualTo(num);
            num++;
        }

    }

    private StudyBenefitRequestDto createStudyBenefitRequestDto(Long id, Long num, String content) {
        StudyBenefitRequestDto dto = new StudyBenefitRequestDto();
        dto.setId(id);
        dto.setBenefitNum(num);
        dto.setContent(content);
        return dto;
    }
}