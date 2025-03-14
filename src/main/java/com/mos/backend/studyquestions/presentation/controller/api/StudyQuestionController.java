package com.mos.backend.studyquestions.presentation.controller.api;

import com.mos.backend.studyquestions.application.StudyQuestionService;
import com.mos.backend.studyquestions.application.responsedto.StudyQuestionResponseDto;
import com.mos.backend.studyquestions.presentation.requestdto.StudyQuestionCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyQuestionController {

    private final StudyQuestionService studyQuestionService;

    /**
     * 스터디 지원 질문 생성|수정|삭제
     */

    @PostMapping("/studies/{studyId}/questions")
    public List<StudyQuestionResponseDto> createOrUpdateOrDelete(@PathVariable Long studyId, @RequestBody List<StudyQuestionCreateRequestDto> requestDto) {
        return studyQuestionService.createOrUpdateOrDelete(studyId, requestDto);
    }

    /**
     * 스터디 단 건 조회
     */

    @GetMapping("/studies/{studyId}/questions/{questionId}")
    public StudyQuestionResponseDto get(@PathVariable Long studyId, @PathVariable Long questionId) {
        return studyQuestionService.get(studyId, questionId);
    }

    /**
     * 스터디 다 건 조회
     */

    @GetMapping("/studies/{studyId}/questions")
    public List<StudyQuestionResponseDto> getAll(@PathVariable Long studyId) {
        return studyQuestionService.getAll(studyId);
    }
}
