package com.mos.backend.studyquestions.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyquestions.application.responsedto.StudyQuestionResponseDto;
import com.mos.backend.studyquestions.entity.QuestionType;
import com.mos.backend.studyquestions.entity.StudyQuestion;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudyQuestionService 테스트")
class StudyQuestionServiceTest {
    @Mock
    private StudyQuestionRepository studyQuestionRepository;

    @Mock
    private EntityFacade entityFacade;

    @InjectMocks
    private StudyQuestionService studyQuestionService;

    @Nested
    @DisplayName("createOrUpdateOrDelete() 테스트")
    class CreateOrUpdateOrDeleteTest {

        @Test
        @DisplayName("questionNum 순서가 빈 값이 존재하면 에러를 발생시킨다.")
        void nullQuestionNumCreateOrUpdateOrDelete_ThrowsException() {
            // given
            Long studyId = 1L;
            List<StudyQuestionCreateRequestDto> contents = List.of(
                    new StudyQuestionCreateRequestDto(null, 1L, "질문1", true, "주관식", List.of()),
                    new StudyQuestionCreateRequestDto(null, null, "질문2", true, "객관식", List.of("옵션1", "옵션2"))
            );

            // when
            MosException mosException = assertThrows(MosException.class, () -> studyQuestionService.createOrUpdateOrDelete(studyId, contents));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyQuestionErrorCode.INVALID_QUESTION_NUM);
        }

        @Test
        @DisplayName("ruleNum 순서가 연속된 수가 아니라면에러를 발생시킨다.")
        void notContinuousRuleNumCreateOrUpdateOrDelete_ThrowsException() {
            // given
            Long studyId = 1L;
            List<StudyQuestionCreateRequestDto> contents = List.of(
                    new StudyQuestionCreateRequestDto(null, 1L, "질문1", true, "주관식", List.of()),
                    new StudyQuestionCreateRequestDto(null, 3L, "질문2", true, "객관식", List.of("옵션1", "옵션2"))
            );

            // when
            MosException mosException = assertThrows(MosException.class, () -> studyQuestionService.createOrUpdateOrDelete(studyId, contents));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyQuestionErrorCode.INVALID_QUESTION_NUM);
        }

        @Test
        @DisplayName("ruleNum 순서가 1부터 시작되지 않으면 에러를 발생시킨다.")
        void ruleNumNotStartFrom1CreateOrUpdateOrDelete_ThrowsException() {
            // given
            Long studyId = 1L;
            List<StudyQuestionCreateRequestDto> contents = List.of(
                    new StudyQuestionCreateRequestDto(null, 1L, "질문2", true, "주관식", List.of()),
                    new StudyQuestionCreateRequestDto(null, 3L, "질문3", true, "객관식", List.of("옵션1", "옵션2"))
            );

            // when
            MosException mosException = assertThrows(MosException.class, () -> studyQuestionService.createOrUpdateOrDelete(studyId, contents));

            // then
            assertThat(mosException.getErrorCode()).isEqualTo(StudyQuestionErrorCode.INVALID_QUESTION_NUM);
        }

        @Test
        @DisplayName("중복된 questionNum 입력 시 예외 발생")
        void createOrUpdateOrDelete_DuplicateQuestionNum() {
            // Given
            Long studyId = 1L;
            List<StudyQuestionCreateRequestDto> requestDtos = List.of(
                    new StudyQuestionCreateRequestDto(null, 1L, "질문1", true, "주관식", List.of()),
                    new StudyQuestionCreateRequestDto(null, 1L, "질문2", true, "객관식", List.of("옵션1", "옵션2"))
            );

            // When
            MosException exception = assertThrows(MosException.class, () -> {
                studyQuestionService.createOrUpdateOrDelete(studyId, requestDtos);
            });

            // then
            assertEquals(StudyQuestionErrorCode.INVALID_QUESTION_NUM, exception.getErrorCode());
        }


        @Test
        @DisplayName("새로운 질문 생성 성공")
        void createStudyQuestion_Success() {
            //given
            Long studyId = 1L;
            List<StudyQuestionCreateRequestDto> requestDtos = List.of(
                    new StudyQuestionCreateRequestDto(null, 1L, "질문1", true, "주관식", List.of()),
                    new StudyQuestionCreateRequestDto(null, 2L, "질문2", true, "객관식", List.of("옵션1", "옵션2"))
            );
            Study mockStudy = Study.builder().id(studyId).build();
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyQuestionRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>());

            //when
            studyQuestionService.createOrUpdateOrDelete(studyId, requestDtos);

            //then
            verify(studyQuestionRepository, times(2)).save(any(StudyQuestion.class));
        }

        @Test
        @DisplayName("질문 수정 성공")
        void updateStudyQuestion_Success() {
            //given
            Long studyId = 1L;
            Study mockStudy = Study.builder().id(studyId).build();

            // 수정할 질문 목록
            List<StudyQuestionCreateRequestDto> requestDtos = List.of(
                    new StudyQuestionCreateRequestDto(1L, 1L, "수정된 질문1", true, "객관식", List.of("옵션1", "옵션2"))
            );

            // 수정 전 질문 생성
            StudyQuestion studyQuestion1 = spy(StudyQuestion.create(mockStudy, 1L, "질문1", "주관식", List.of(), true));
            doReturn(1L).when(studyQuestion1).getId();
            StudyQuestion studyQuestion2 = spy(StudyQuestion.create(mockStudy, 2L, "질문2", "주관식", List.of(), true));
            doReturn(2L).when(studyQuestion2).getId();

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyQuestionRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>(List.of(studyQuestion1, studyQuestion2)));
            when(studyQuestionRepository.findByIdAndStudy(1L, mockStudy)).thenReturn(Optional.of(studyQuestion1));

            //when
            studyQuestionService.createOrUpdateOrDelete(studyId, requestDtos);

            //then
            verify(studyQuestion1).changeQuestion(1L, "수정된 질문1", true, "객관식", List.of("옵션1", "옵션2"));
            assertThat(studyQuestion1.getQuestion()).isEqualTo("수정된 질문1");
            assertThat(studyQuestion1.isRequired()).isTrue();
            assertThat(studyQuestion1.getType()).isEqualTo(QuestionType.MULTIPLE_CHOICE);
            assertThat(studyQuestion1.getOptions().toList()).isEqualTo(List.of("옵션1", "옵션2"));
        }

        @Test
        @DisplayName("질문 삭제")
        void deleteStudyQuestion_Success() {
            //given
            Long studyId = 1L;
            Study mockStudy = Study.builder().id(studyId).build();

            // 수정할 질문 목록
            List<StudyQuestionCreateRequestDto> requestDtos = List.of();

            StudyQuestion studyQuestion1 = spy(StudyQuestion.create(mockStudy, 1L, "질문1", "주관식", List.of(), true));
            doReturn(1L).when(studyQuestion1).getId();

            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyQuestionRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>(List.of(studyQuestion1)));

            //when
            studyQuestionService.createOrUpdateOrDelete(studyId, requestDtos);

            //then
            verify(studyQuestionRepository).deleteAll(List.of(studyQuestion1));
        }

        @Test
        @DisplayName("스터디가 존재하지 않을 때 MosException 발생")
        void createOrUpdateOrDelete_StudyNotFound() {
            // Given
            Long studyId = 1L;
            List<StudyQuestionCreateRequestDto> requestDtos = List.of(
                    new StudyQuestionCreateRequestDto(null, 1L, "질문1", true, "주관식", List.of()),
                    new StudyQuestionCreateRequestDto(null, 2L, "질문2", true, "객관식", List.of("옵션1", "옵션2"))
            );

            when(entityFacade.getStudy(studyId)).thenThrow(new MosException(StudyQuestionErrorCode.STUDY_QUESTION_NOT_FOUND));

            // When & Then
            MosException exception = assertThrows(MosException.class, () -> {
                studyQuestionService.createOrUpdateOrDelete(studyId, requestDtos);
            });

            assertThat(exception.getErrorCode()).isEqualTo(StudyQuestionErrorCode.STUDY_QUESTION_NOT_FOUND);
        }

        @Test
        @DisplayName("찾을 수 없는 studyQuestion")
        void createOrUpdateOrDelete_invalidStudyQuestionId() {
            // Given
            Long studyId = 1L;
            Long questionId = 1L;
            List<StudyQuestionCreateRequestDto> requestDtos = List.of(
                    new StudyQuestionCreateRequestDto(questionId, 1L, "질문1", true, "주관식", List.of()));

            Study mockStudy = Study.builder().id(studyId).build();
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
            when(studyQuestionRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>());
            when(studyQuestionRepository.findByIdAndStudy(questionId, mockStudy)).thenReturn(Optional.empty());

            // When
            MosException exception = assertThrows(MosException.class, () -> {
                studyQuestionService.createOrUpdateOrDelete(studyId, requestDtos);
            });

            // then
            assertThat(exception.getErrorCode()).isEqualTo(StudyQuestionErrorCode.STUDY_QUESTION_NOT_FOUND);
        }

        @Test
        @DisplayName("단답형인데 option이 있는 경우 예외 발생")
        void createOrUpdateOrDelete_shortAnswerWithOption() {
            //given
            Long studyId = 1L;
            List<StudyQuestionCreateRequestDto> requestDtos = List.of(
                    new StudyQuestionCreateRequestDto(null, 1L, "질문1", true, "주관식", List.of("옵션1", "옵션2")));
            Study mockStudy = Study.builder().id(studyId).build();
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);

            //when
            MosException exception = assertThrows(MosException.class, () -> {
                studyQuestionService.createOrUpdateOrDelete(studyId, requestDtos);
            });

            //then
            assertThat(exception.getErrorCode()).isEqualTo(StudyQuestionErrorCode.INVALID_SHORT_ANSWER);
        }

        @Test
        @DisplayName("객관식인데 option이 2개 미만인 경우 예외 발생")
        void createOrUpdateOrDelete_multipleChoiceWithoutTwoOptions() {
            //given
            Long studyId = 1L;
            List<StudyQuestionCreateRequestDto> requestDtos = List.of(
                    new StudyQuestionCreateRequestDto(null, 1L, "질문1", true, "객관식", List.of("옵션1")));
            Study mockStudy = Study.builder().id(studyId).build();
            when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);

            //when
            MosException exception = assertThrows(MosException.class, () -> {
                studyQuestionService.createOrUpdateOrDelete(studyId, requestDtos);
            });

            //then
            assertThat(exception.getErrorCode()).isEqualTo(StudyQuestionErrorCode.INVALID_MULTIPLE_CHOICE_OPTIONS);
        }
    }

    @Test
    @DisplayName("단건 조회 성공")
    void getStudyQuestion_Success() {
        //given
        Long studyId = 1L;
        Long questionId = 1L;
        Study mockStudy = Study.builder().id(studyId).build();
        StudyQuestion studyQuestion = spy(StudyQuestion.create(mockStudy, 1L, "질문1", "주관식", List.of(), true));
        doReturn(questionId).when(studyQuestion).getId();
        when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
        when(studyQuestionRepository.findByIdAndStudy(questionId, mockStudy)).thenReturn(Optional.of(studyQuestion));

        //when
        StudyQuestionResponseDto responseDto = studyQuestionService.get(studyId, questionId);

        //then
        assertThat(responseDto.getId()).isEqualTo(questionId);
        assertThat(responseDto.getQuestion()).isEqualTo("질문1");
        assertThat(responseDto.getType()).isEqualTo("주관식");
        assertThat(responseDto.isRequired()).isTrue();
        assertThat(responseDto.getQuestionNum()).isEqualTo(1L);
        assertThat(responseDto.getOptions()).isEmpty();
    }

    @Test
    @DisplayName("다 건 조회 성공")
    void getAllStudyQuestion_Success() {
        //given
        Long studyId = 1L;
        Long questionId1 = 1L;
        Long questionId2 = 2L;
        Study mockStudy = Study.builder().id(studyId).build();
        StudyQuestion studyQuestion1 = spy(StudyQuestion.create(mockStudy, 1L, "질문1", "주관식", List.of(), false));
        doReturn(questionId1).when(studyQuestion1).getId();
        StudyQuestion studyQuestion2 = spy(StudyQuestion.create(mockStudy, 2L, "질문2", "객관식", List.of("보기1", "보기2"), true));
        doReturn(questionId2).when(studyQuestion2).getId();
        when(entityFacade.getStudy(studyId)).thenReturn(mockStudy);
        when(studyQuestionRepository.findAllByStudy(mockStudy)).thenReturn(new ArrayList<>(List.of(studyQuestion1, studyQuestion2)));

        //when
        List<StudyQuestionResponseDto> responseDtoList = studyQuestionService.getAll(studyId);

        //then
        assertThat(responseDtoList.get(0).getId()).isEqualTo(questionId1);
        assertThat(responseDtoList.get(0).getQuestion()).isEqualTo("질문1");
        assertThat(responseDtoList.get(0).getType()).isEqualTo("주관식");
        assertThat(responseDtoList.get(0).isRequired()).isFalse();
        assertThat(responseDtoList.get(0).getQuestionNum()).isEqualTo(1L);
        assertThat(responseDtoList.get(0).getOptions()).isEmpty();

        assertThat(responseDtoList.get(1).getId()).isEqualTo(questionId2);
        assertThat(responseDtoList.get(1).getQuestion()).isEqualTo("질문2");
        assertThat(responseDtoList.get(1).getType()).isEqualTo("객관식");
        assertThat(responseDtoList.get(1).isRequired()).isTrue();
        assertThat(responseDtoList.get(1).getQuestionNum()).isEqualTo(2L);
        assertThat(responseDtoList.get(1).getOptions()).hasSize(2);
    }
}

