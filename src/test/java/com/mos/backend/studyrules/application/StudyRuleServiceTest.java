package com.mos.backend.studyrules.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studycurriculum.application.StudyCurriculumService;
import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import com.mos.backend.studycurriculum.infrastructure.StudyCurriculumRepository;
import com.mos.backend.studycurriculum.presentation.requestdto.StudyCurriculumCreateRequestDto;
import com.mos.backend.studyrules.application.responsedto.StudyRuleResponseDto;
import com.mos.backend.studyrules.entity.StudyRule;
import com.mos.backend.studyrules.entity.exception.StudyRuleErrorCode;
import com.mos.backend.studyrules.infrastructure.StudyRuleRepository;
import com.mos.backend.studyrules.presentation.requestdto.StudyRuleCreateRequestDto;
import org.assertj.core.api.Assertions;
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

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudyRuleService 테스트")
class StudyRuleServiceTest {

    @Mock
    private StudyRuleRepository studyRuleRepository;

    @Mock
    private EntityFacade entityFacade;

    @InjectMocks
    private StudyRuleService studyRuleService;


    @Nested
    @DisplayName("스터디 룰 생성, 수정, 삭제 성공 시나리오")
    class SuccessScenarios {

        @Test
        @DisplayName("ruleNum 순서가 빈 값이 존재하면 테스트를 실패한다.")
        void InvalidRuleNumCreateOrUpdateOrDelete_Fail() {
            // given
            Long studyId = 1L;
            List<StudyRuleCreateRequestDto> contents = List.of(
                    new StudyRuleCreateRequestDto(null, 1L, "스터디 룰1"),
                    new StudyRuleCreateRequestDto(null, null, "스터디 룰2")
            );

            // when
            MosException mosException = assertThrows(MosException.class, () -> studyRuleService.createOrUpdateOrDelete(studyId, contents));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyRuleErrorCode.INVALID_RULE_NUM);

        }

        @Test
        @DisplayName("새로운 스터디 룰 생성")
        void createStudyRuleCreate_Success() {
            // Given
            Long studyId = 1L;
            List<StudyRuleCreateRequestDto> contents = List.of(
                    new StudyRuleCreateRequestDto(null, 1L, "스터디 룰1"),
                    new StudyRuleCreateRequestDto(null, 2L, "스터디 룰2")
            );
            Study mockStudy = Study.builder().id(studyId).build();
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyRuleRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>());
            // When
            studyRuleService.createOrUpdateOrDelete(studyId, contents);

            // Then
            verify(studyRuleRepository, times(2)).save(any(StudyRule.class));
        }

        @Test
        @DisplayName("스터디 룰 업데이트")
        void updateStudyRule_Success() {
            // Given
            Long studyId = 1L;
            List<StudyRuleCreateRequestDto> contents = List.of(
                    new StudyRuleCreateRequestDto(1L, 1L, "스터디 룰1 수정")
            );
            Study mockStudy = Study.builder().id(studyId).build();
            StudyRule studyRule = spy(StudyRule.create(mockStudy, 1L, "스터디 1"));
            doReturn(1L).when(studyRule).getId();

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyRuleRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>(List.of(studyRule)));
            when(studyRuleRepository.findByIdAndStudy(1L, mockStudy)).thenReturn(Optional.of(studyRule));
            // When
            studyRuleService.createOrUpdateOrDelete(studyId, contents);

            // Then
            verify(studyRule).update(1L, "스터디 룰1 수정");
        }

        @Test
        @DisplayName("스터디 룰 삭제")
        void deleteStudyRule_Success() {
            // Given
            Long studyId = 1L;
            List<StudyRuleCreateRequestDto> contents = List.of(
            );
            Study mockStudy = Study.builder().id(studyId).build();
            StudyRule studyRule = spy(StudyRule.create(mockStudy, 1L, "스터디 1"));
            doReturn(1L).when(studyRule).getId();

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyRuleRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>(List.of(studyRule)));
            // When
            studyRuleService.createOrUpdateOrDelete(studyId, contents);

            // Then
            verify(studyRuleRepository).deleteAll(List.of(studyRule));
        }
    }


    @Nested
    @DisplayName("스터디 단 건 조회 테스트")
    class getOneScenarios {
        @Test
        @DisplayName("스터디 룰 단 건 조회 성공")
        void getStudyRule_Success() {
            // given
            Long studyId = 1L;
            Long studyRuleId = 1L;

            Study mockStudy = Study.builder().id(studyId).build();
            StudyRule studyRule = spy(StudyRule.create(mockStudy, 2L, "스터디 1"));
            doReturn(1L).when(studyRule).getId();

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyRuleRepository.findByIdAndStudy(studyRuleId, mockStudy)).thenReturn(Optional.of(studyRule));


            // when
            StudyRuleResponseDto studyRuleResponseDto = studyRuleService.get(studyId, studyRuleId);

            // then
            assertThat(studyRuleResponseDto.getId()).isEqualTo(studyRuleId);
            assertThat(studyRuleResponseDto.getRuleNum()).isEqualTo(2L);
            assertThat(studyRuleResponseDto.getContent()).isEqualTo("스터디 1");
        }

        @Test
        @DisplayName("스터디 ID와 스터디 룰 id가 상호 연관관계가 존재하지 않으면 스터디 조회를 실패한다.")
        void InvalidmatchStudyIdWithStudyRuleIdGetStudyRule_Fail() {
            // given
            Long studyId = 1L;
            Long studyRuleId = 1L;

            Study mockStudy = Study.builder().id(studyId).build();

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyRuleRepository.findByIdAndStudy(studyRuleId, mockStudy)).thenReturn(Optional.empty());

            // when
            MosException mosException = assertThrows(MosException.class, () -> studyRuleService.get(studyId, studyRuleId));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyRuleErrorCode.STUDY_RULE_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("스터디 룰 다 건 조회 성공")
    void getStudyRules_Success() {
        // Given
        Long studyId = 1L;
        Study mockStudy = Study.builder().id(studyId).build();
        StudyRule studyRule1 = StudyRule.create(mockStudy, 1L, "스터디 룰1");
        StudyRule studyRule2 = StudyRule.create(mockStudy, 2L, "스터디 룰2");

        when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
        when(studyRuleRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>(List.of(studyRule1, studyRule2)));

        // When
        List<StudyRuleResponseDto> all = studyRuleService.getAll(studyId);

        // Then
        assertThat(all).hasSize(2);
    }

}