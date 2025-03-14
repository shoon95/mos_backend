package com.mos.backend.studyquestions.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyquestions.application.responsedto.StudyQuestionResponseDto;
import com.mos.backend.studyquestions.entity.StudyQuestion;
import com.mos.backend.studyquestions.entity.StudyQuestionErrorCode;
import com.mos.backend.studyquestions.infrastructure.StudyQuestionRepository;
import com.mos.backend.studyquestions.presentation.requestdto.StudyQuestionCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyQuestionService {

    private final StudyQuestionRepository studyQuestionRepository;
    private final EntityFacade entityFacade;

    /**
     * 스터디 지원 질문 생성|수정|삭제
     */

    @Transactional
    public List<StudyQuestionResponseDto> createOrUpdateOrDelete(Long studyId, List<StudyQuestionCreateRequestDto> requestDtoList) {

        validateRequest(requestDtoList);

        Study study = entityFacade.getStudy(studyId);
        List<StudyQuestion> questionList = studyQuestionRepository.findAllByStudy(study);

        // 요청에 없는 항목은 삭제
        List<StudyQuestion> deleteQuestionList = getDeleteQuestionList(requestDtoList, questionList);
        studyQuestionRepository.deleteAll(deleteQuestionList);

        // id가 없으면 새로 저장, 있으면 업데이트
        requestDtoList.forEach(requestDto -> {
            if (requestDto.getId() == null) {
                studyQuestionRepository.save(convertToEntity(study, requestDto));
            } else {
                StudyQuestion studyQuestion = findByIdAndStudy(requestDto.getId(), study);
                studyQuestion.changeQuestion(requestDto.getQuestionNum(), requestDto.getQuestion(), requestDto.isRequired(), requestDto.getType(), requestDto.getOptions());
            }
        });
        return getAll(studyId);
    }

    /**
     * 단 건 조회
     */

    public StudyQuestionResponseDto get(Long studyId, Long questionId) {
        Study study = entityFacade.getStudy(studyId);
        StudyQuestion studyQuestion = studyQuestionRepository.findByIdAndStudy(questionId, study).orElseThrow(() -> new MosException(StudyQuestionErrorCode.STUDY_QUESTION_NOT_FOUND));
        return StudyQuestionResponseDto.from(studyQuestion);
    }

    /**
     * 다 건 조회
     */

    public List<StudyQuestionResponseDto> getAll(Long studyId) {
        Study study = entityFacade.getStudy(studyId);
        List<StudyQuestion> questionList = studyQuestionRepository.findAllByStudy(study);
        questionList.sort(Comparator.comparing(StudyQuestion::getQuestionNum));
        return questionList.stream().map(StudyQuestionResponseDto::from).toList();
    }

    private void validateRequest(List<StudyQuestionCreateRequestDto> questionCreateRequestDtoList) {
        validateQuestionNum(questionCreateRequestDtoList);
    }

    private void validateQuestionNum(List<StudyQuestionCreateRequestDto> questionRequestDtoList) {
        Set<Long> questionNumSet = questionRequestDtoList.stream()
                .map(q -> q.getQuestionNum())
                .collect(Collectors.toSet());
        if (questionNumSet.size() != questionRequestDtoList.size()) {
            throw new MosException(StudyQuestionErrorCode.INVALID_QUESTION_NUM);
        }
    }

    private List<StudyQuestion> getDeleteQuestionList(List<StudyQuestionCreateRequestDto> requestDtoList, List<StudyQuestion> questionList) {
        List<Long> requestDto = requestDtoList.stream()
                .map(StudyQuestionCreateRequestDto::getId)
                .filter(Objects::nonNull)
                .toList();
        return questionList.stream()
                .filter(q -> !requestDto.contains(q.getId()))
                .toList();
    }

    private StudyQuestion convertToEntity(Study study, StudyQuestionCreateRequestDto dto) {
        return StudyQuestion.create(
                study,
                dto.getQuestionNum(),
                dto.getQuestion(),
                dto.getType(),
                dto.getOptions(),
                dto.isRequired()
        );
    }

    private StudyQuestion findByIdAndStudy(long questionId, Study study) {
        return studyQuestionRepository.findByIdAndStudy(questionId, study).orElseThrow(() -> new MosException(StudyQuestionErrorCode.STUDY_QUESTION_NOT_FOUND));
    }
}
