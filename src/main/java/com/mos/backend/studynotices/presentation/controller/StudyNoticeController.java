package com.mos.backend.studynotices.presentation.controller;

import com.mos.backend.studynotices.application.StudyNoticeService;
import com.mos.backend.studynotices.application.responsedto.StudyNoticeResponseDto;
import com.mos.backend.studynotices.presentation.requestdto.StudyNoticeCreateRequestDto;
import com.mos.backend.studynotices.presentation.requestdto.StudyNoticeUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StudyNoticeController {

    private final StudyNoticeService studyNoticeService;

    /**
     * 공지 생성
     */

    @PostMapping("/studies/{studyId}/notices")
    @ResponseStatus(HttpStatus.CREATED)
    public StudyNoticeResponseDto create(
            @PathVariable Long studyId,
            @RequestBody @Valid StudyNoticeCreateRequestDto requestDto,
            @AuthenticationPrincipal Long currentUserId) {
        return studyNoticeService.create(studyId, currentUserId, requestDto.getTitle(), requestDto.getContent(), requestDto.getPinned(), requestDto.getImportant());
    }

    /**
     * 공지 수정
     */

    @PatchMapping("/studies/{studyId}/notices/{noticeId}")
    @ResponseStatus(HttpStatus.OK)
    public StudyNoticeResponseDto update(
            @PathVariable Long studyId,
            @PathVariable Long noticeId,
            @RequestBody @Valid StudyNoticeUpdateRequestDto requestDto) {
        return studyNoticeService.update(studyId, noticeId, requestDto.getTitle(), requestDto.getContent(), requestDto.getPinned(), requestDto.getImportant());
    }

    /**
     * 공지 삭제
     */

    @DeleteMapping("/studies/{studyId}/notices/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long studyId, @PathVariable Long noticeId) {
        studyNoticeService.delete(studyId, noticeId);
    }

    /**
     * 공지 단 건 조회
     */

    @GetMapping("/studies/{studyId}/notices/{noticeId}")
    @ResponseStatus(HttpStatus.OK)
    public StudyNoticeResponseDto readOne(@PathVariable Long studyId, @PathVariable Long noticeId) {
        return studyNoticeService.readOne(studyId, noticeId);
    }

    /**
     * 공지 다 건 조회
     */

    @GetMapping("/studies/{studyId}/notices")
    @ResponseStatus(HttpStatus.OK)
    public List<StudyNoticeResponseDto> readAll(@PathVariable Long studyId) {
        return studyNoticeService.readAll(studyId);
    }
}
