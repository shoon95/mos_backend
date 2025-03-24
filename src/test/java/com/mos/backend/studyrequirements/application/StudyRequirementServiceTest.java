package com.mos.backend.studyrequirements.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyrequirements.application.responsedto.StudyRequirementResponseDto;
import com.mos.backend.studyrequirements.entity.StudyRequirement;
import com.mos.backend.studyrequirements.entity.exception.StudyRequirementErrorCode;
import com.mos.backend.studyrequirements.infrastructure.StudyRequirementRepository;
import com.mos.backend.studyrequirements.presentation.requestdto.StudyRequirementCreateRequestDto;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudyRequirementService 테스트")
class StudyRequirementServiceTest {

    @Mock
    private StudyRequirementRepository studyRequirementRepository;

    @Mock
    private EntityFacade entityFacade;

    @InjectMocks
    private StudyRequirementService studyRequirementService;

    @Nested
    @DisplayName("스터디 요구 사항 생성, 수정, 삭제 시나리오")
    class CreateOrUpdateOrDeleteTest {

        @Test
        @DisplayName("requirementNum 순서가 빈 값이 존재하면 에러를 발생시킨다.")
        void nullRequirementNumCreateOrUpdateOrDelete_ThrowsException() {
            // given
            Long studyId = 1L;
            List<StudyRequirementCreateRequestDto> contents = List.of(
                    new StudyRequirementCreateRequestDto(null, 1L, "스터디 요구사항1"),
                    new StudyRequirementCreateRequestDto(null, null, "스터디 요구사항2")
            );

            // when
            MosException mosException = assertThrows(MosException.class, () -> studyRequirementService.createOrUpdateOrDelete(studyId, contents));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyRequirementErrorCode.INVALID_REQUIREMENT_NUM);
        }

        @Test
        @DisplayName("requirementNum 순서가 연속된 수가 아니라면에러를 발생시킨다.")
        void notContinuousRequirementNumCreateOrUpdateOrDelete_ThrowsException() {
            // given
            Long studyId = 1L;
            List<StudyRequirementCreateRequestDto> contents = List.of(
                    new StudyRequirementCreateRequestDto(null, 1L, "스터디 요구사항1"),
                    new StudyRequirementCreateRequestDto(null, 3L, "스터디 요구사항2")
            );


            // when
            MosException mosException = assertThrows(MosException.class, () -> studyRequirementService.createOrUpdateOrDelete(studyId, contents));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyRequirementErrorCode.INVALID_REQUIREMENT_NUM);
        }

        @Test
        @DisplayName("requirementNum 순서가 1부터 시작되지 않으면 에러를 발생시킨다.")
        void requirementNumNotStartFrom1CreateOrUpdateOrDelete_ThrowsException() {
            // given
            Long studyId = 1L;
            List<StudyRequirementCreateRequestDto> contents = List.of(
                    new StudyRequirementCreateRequestDto(null, 2L, "스터디 요구사항1"),
                    new StudyRequirementCreateRequestDto(null, 3L, "스터디 요구사항2")
            );


            // when
            MosException mosException = assertThrows(MosException.class, () -> studyRequirementService.createOrUpdateOrDelete(studyId, contents));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyRequirementErrorCode.INVALID_REQUIREMENT_NUM);
        }

        @Test
        @DisplayName("requirementNum 순서가 중복이면 에러를 발생시킨다.")
        void duplicatedRequirementNumCreateOrUpdateOrDelete_ThrowsException() {
            // given
            Long studyId = 1L;
            List<StudyRequirementCreateRequestDto> contents = List.of(
                    new StudyRequirementCreateRequestDto(null, 2L, "스터디 요구사항1"),
                    new StudyRequirementCreateRequestDto(null, 2L, "스터디 요구사항2")
            );


            // when
            MosException mosException = assertThrows(MosException.class, () -> studyRequirementService.createOrUpdateOrDelete(studyId, contents));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyRequirementErrorCode.INVALID_REQUIREMENT_NUM);
        }

        @Test
        @DisplayName("새로운 스터디 요구사항 생성 성공")
        void createStudyRequirementCreate_Success() {
            // Given
            Long studyId = 1L;
            List<StudyRequirementCreateRequestDto> contents = List.of(
                    new StudyRequirementCreateRequestDto(null, 1L, "스터디 요구사항1"),
                    new StudyRequirementCreateRequestDto(null, 2L, "스터디 요구사항2")
            );
            Study mockStudy = Study.builder().id(studyId).build();
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyRequirementRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>());
            // When
            studyRequirementService.createOrUpdateOrDelete(studyId, contents);

            // Then
            verify(studyRequirementRepository, times(2)).save(any(StudyRequirement.class));
        }

        @Test
        @DisplayName("스터디 요구사항 업데이트")
        void updateStudyRequirement_Success() {
            // Given
            Long studyId = 1L;
            List<StudyRequirementCreateRequestDto> contents = List.of(
                    new StudyRequirementCreateRequestDto(1L, 1L, "스터디 요구사항1 수정")
            );
            Study mockStudy = Study.builder().id(studyId).build();
            StudyRequirement studyRequirement = spy(StudyRequirement.create(mockStudy, 1L, "스터디 1"));
            doReturn(1L).when(studyRequirement).getId();

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyRequirementRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>(List.of(studyRequirement)));
            when(studyRequirementRepository.findByIdAndStudy(1L, mockStudy)).thenReturn(Optional.of(studyRequirement));
            // When
            studyRequirementService.createOrUpdateOrDelete(studyId, contents);

            // Then
            verify(studyRequirement).update(1L, "스터디 요구사항1 수정");
        }

        @Test
        @DisplayName("스터디 요구사항 삭제")
        void deleteStudyRequirement_Success() {
            // Given
            Long studyId = 1L;
            List<StudyRequirementCreateRequestDto> contents = List.of(
            );
            Study mockStudy = Study.builder().id(studyId).build();
            StudyRequirement studyRequirement = spy(StudyRequirement.create(mockStudy, 1L, "스터디 1"));
            doReturn(1L).when(studyRequirement).getId();

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyRequirementRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>(List.of(studyRequirement)));
            // When
            studyRequirementService.createOrUpdateOrDelete(studyId, contents);

            // Then
            verify(studyRequirementRepository).deleteAll(List.of(studyRequirement));
        }
    }


    @Nested
    @DisplayName("스터디 요구사항 단 건 조회 테스트")
    class getOneScenarios {
        @Test
        @DisplayName("스터디 요구사항 단 건 조회 성공")
        void getStudyRequirement_Success() {
            // given
            Long studyId = 1L;
            Long studyRequirementId = 1L;

            Study mockStudy = Study.builder().id(studyId).build();
            StudyRequirement studyRequirement = spy(StudyRequirement.create(mockStudy, 2L, "스터디 1"));
            doReturn(1L).when(studyRequirement).getId();

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyRequirementRepository.findByIdAndStudy(studyRequirementId, mockStudy)).thenReturn(Optional.of(studyRequirement));


            // when
            StudyRequirementResponseDto studyRequirementResponseDto = studyRequirementService.get(studyId, studyRequirementId);

            // then
            assertThat(studyRequirementResponseDto.getId()).isEqualTo(studyRequirementId);
            assertThat(studyRequirementResponseDto.getRequirementNum()).isEqualTo(2L);
            assertThat(studyRequirementResponseDto.getContent()).isEqualTo("스터디 1");
        }

        @Test
        @DisplayName("스터디 ID와 스터디 요구사항 id가 상호 연관관계가 존재하지 않으면 스터디 조회를 실패한다.")
        void invalidMatchStudyIdWithStudyRequirementIdGetStudyRequirement_Fail() {
            // given
            Long studyId = 1L;
            Long studyRequirementId = 1L;

            Study mockStudy = Study.builder().id(studyId).build();

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyRequirementRepository.findByIdAndStudy(studyRequirementId, mockStudy)).thenReturn(Optional.empty());

            // when
            MosException mosException = assertThrows(MosException.class, () -> studyRequirementService.get(studyId, studyRequirementId));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyRequirementErrorCode.STUDY_REQUIREMENT_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("스터디 요구사항 다 건 조회 성공")
    void getStudyRequirement_Success() {
        // Given
        Long studyId = 1L;
        Study mockStudy = Study.builder().id(studyId).build();
        StudyRequirement studyRequirement1 = StudyRequirement.create(mockStudy, 1L, "스터디 요구사항1");
        StudyRequirement studyRequirement12 = StudyRequirement.create(mockStudy, 2L, "스터디 요구사항2");

        when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
        when(studyRequirementRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>(List.of(studyRequirement1, studyRequirement12)));

        // When
        List<StudyRequirementResponseDto> all = studyRequirementService.getAll(studyId);

        // Then
        assertThat(all).hasSize(2);
    }

}