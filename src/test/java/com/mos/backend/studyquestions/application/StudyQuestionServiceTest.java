package com.mos.backend.studyquestions.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studyquestions.entity.StudyQuestionErrorCode;
import com.mos.backend.studyquestions.infrastructure.StudyQuestionRepository;
import com.mos.backend.studyquestions.presentation.requestdto.StudyQuestionCreateRequestDto;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudyQuestionService 테스트")
class StudyQuestionServiceTest {
    @Mock
    private StudyQuestionRepository studyQuestionRepository;

    @Mock
    private StudyRepository studyRepository;

    @InjectMocks
    private StudyQuestionService studyQuestionService;

    @Nested
    @DisplayName("스터디 질문 생성 성공 시나리오")
    class SuccessScenarios {
        @Test
        @DisplayName("정상적인 스터디 질문 생성 시 성공")
        void givenValidRequest_whenCreateStudyQuestion_thenSuccess() {
            // given
            Long studyId = 1L;
            Study mockStudy = mock(Study.class);
            List<StudyQuestionCreateRequestDto> requestDtos = new ArrayList<>();
            requestDtos.add(new StudyQuestionCreateRequestDto("질문1", 1L, true, "객관식", List.of("옵션1", "옵션2")));
            requestDtos.add(new StudyQuestionCreateRequestDto("질문2", 2L, false, "주관식", List.of()));

            //when
            when(studyRepository.findById(studyId)).thenReturn(Optional.of(mockStudy));

            studyQuestionService.create(studyId, requestDtos);

            // then
            verify(studyRepository).findById(studyId);
            verify(studyQuestionRepository).saveAll(anyList());
        }

        @Test
        @DisplayName("빈 리스트를 넣을 때 아무것도 실행되지 않는다.")
        void givenEmptyRequestList_whenCreateStudyQuestion_thenNotExecuteRepositoryMethod() {
            //given
            Long studyId = 1L;
            List<StudyQuestionCreateRequestDto> requestDtos = new ArrayList<>();

            //when
            studyQuestionService.create(studyId, requestDtos);

            //then
            verify(studyRepository, never()).findById(anyLong());
            verify(studyQuestionRepository, never()).saveAll(anyList());
        }
    }

    @Nested
    @DisplayName("스터디 질문 생성 실패 시나리오")
    class FailureScenarios {
        @Test
        @DisplayName("스터디가 존재하지 않을 때 MosException 발생")
        void givenNoExistingStudy_whenCreateStudyQuestion_thenThrowMosException() {
            // given
            Long studyId = 1L;
            List<StudyQuestionCreateRequestDto> requestDtos = List.of(
                    new StudyQuestionCreateRequestDto("질문1", 1L, true, "객관식", List.of("옵션1", "옵션2"))
            );

            when(studyRepository.findById(studyId)).thenReturn(Optional.empty());

            // when
            MosException exception = assertThrows(MosException.class, () -> {
                studyQuestionService.create(studyId, requestDtos);
            });

            // then
            assertThat(exception.getErrorCode()).isEqualTo(StudyErrorCode.STUDY_NOT_FOUND);
            verify(studyRepository).findById(studyId);
            verify(studyQuestionRepository, never()).saveAll(any());
        }

        @Test
        @DisplayName("객관식 질문의 options가 2개 미만일 때 MosException 발생")
        void givenMultipleChoiceQuestionWithLessThanTwoOptions_whenCreateStudyQuestion_thenThrowMosException() {
            // given
            Long studyId = 1L;
            List<StudyQuestionCreateRequestDto> requestDtos = List.of(
                    new StudyQuestionCreateRequestDto("객관식 질문", 1L, true, "객관식", List.of("옵션1"))
            );

            when(studyRepository.findById(studyId)).thenReturn(Optional.of(mock(Study.class)));

            // when
            MosException exception = assertThrows(MosException.class, () -> {
                studyQuestionService.create(studyId, requestDtos);
            });

            // then
            assertThat(exception.getErrorCode()).isEqualTo(StudyQuestionErrorCode.INVALID_MULTIPLE_CHOICE_OPTIONS);
            verify(studyRepository).findById(studyId);
            verify(studyQuestionRepository, never()).saveAll(any());
        }
        @Test
        @DisplayName("객관식 질문의 options가 null일 때 MosException 발생")
        void givenMultipleChoiceQuestionWithNullOptions_whenCreateStudyQuestion_thenThrowMosException() {
            // given
            Long studyId = 1L;
            List<StudyQuestionCreateRequestDto> requestDtos = List.of(
                    new StudyQuestionCreateRequestDto("객관식 질문", 1L, true, "객관식", null)
            );

            when(studyRepository.findById(studyId)).thenReturn(Optional.of(mock(Study.class)));

            // when
            MosException exception = assertThrows(MosException.class, () -> {
                studyQuestionService.create(studyId, requestDtos);
            });

            // then
            assertThat(exception.getErrorCode()).isEqualTo(StudyQuestionErrorCode.INVALID_MULTIPLE_CHOICE_OPTIONS);
            verify(studyRepository).findById(studyId);
            verify(studyQuestionRepository, never()).saveAll(any());
        }
    }
}