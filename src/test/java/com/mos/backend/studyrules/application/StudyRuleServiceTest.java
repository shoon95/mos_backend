package com.mos.backend.studyrules.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studyrules.infrastructure.StudyRuleRepository;
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
@DisplayName("StudyRuleService 테스트")
class StudyRuleServiceTest {

    @Mock
    private StudyRuleRepository studyRuleRepository;

    @Mock
    private StudyRepository studyRepository;

    @InjectMocks
    private StudyRuleService studyRuleService;

    @Nested
    @DisplayName("스터디 규칙 생성 성공 시나리오")
    class SuccessScenarios {
        @Test
        @DisplayName("정상적인 스터디 규칙 생성")
        void givenValidRequest_whenCreateStudyRule_thenSuccess() {
            // given
            Long studyId = 1L;
            List<String> contents = List.of("규칙1", "규칙2");
            Study mockStudy = mock(Study.class);

            when(studyRepository.findById(studyId)).thenReturn(Optional.of(mockStudy));

            // when
            studyRuleService.create(studyId, contents);

            // then
            verify(studyRepository).findById(studyId);
            verify(studyRuleRepository).saveAll(anyList());
        }
        @Test
        @DisplayName("빈 리스트를 넣을 때 아무것도 실행되지 않는다.")
        void givenEmptyRequestList_whenCreateStudyRule_thenNotExecuteRepositoryMethod() {
            //given
            Long studyId = 1L;
            List<String> contents = new ArrayList<>();

            //when
            studyRuleService.create(studyId, contents);

            //then
            verify(studyRepository, never()).findById(studyId);
            verify(studyRuleRepository, never()).saveAll(anyList());
        }
    }

    @Nested
    @DisplayName("스터디 규칙 생성 실패 시나리오")
    class FailureScenarios {
        @Test
        @DisplayName("스터디가 존재하지 않을 때 MosException 발생")
        void givenNoExistingStudy_whenCreateStudyRule_thenThrowMosException() {
            // given
            Long studyId = 1L;
            List<String> contents = List.of("규칙1", "규칙2");

            when(studyRepository.findById(studyId)).thenReturn(Optional.empty());

            // when
            MosException exception = assertThrows(MosException.class, () -> {
                studyRuleService.create(studyId, contents);
            });

            // then
            assertThat(exception.getErrorCode()).isEqualTo(StudyErrorCode.STUDY_NOT_FOUND);
            verify(studyRepository).findById(studyId);
            verify(studyRuleRepository, never()).saveAll(any());
        }
    }
}