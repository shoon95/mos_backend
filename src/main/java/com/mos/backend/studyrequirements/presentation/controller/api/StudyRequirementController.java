package com.mos.backend.studyrequirements.presentation.controller.api;

import com.mos.backend.studyrequirements.application.StudyRequirementService;
import com.mos.backend.studyrequirements.application.responsedto.StudyRequirementResponseDto;
import com.mos.backend.studyrequirements.presentation.requestdto.StudyRequirementCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyRequirementController {

    private final StudyRequirementService studyRequirementService;


    /**
     * StudyRequirement 생성 | 수정 | 삭제
     */

    @PostMapping("/studies/{studyId}/requirements")
    @ResponseStatus(HttpStatus.OK)
    public List<StudyRequirementResponseDto> createOrUpdateOrDelete(@PathVariable Long studyId, @RequestBody List<StudyRequirementCreateRequestDto> requestDtoList) {
        return studyRequirementService.createOrUpdateOrDelete(studyId, requestDtoList);
    }

    /**
     * StudyRequirement 단 건 조회
     */

    @GetMapping("/studies/{studyId}/requirements/{requirementId}")
    @ResponseStatus(HttpStatus.OK)
    public StudyRequirementResponseDto get(@PathVariable Long studyId, @PathVariable Long requirementId) {
        return studyRequirementService.get(studyId, requirementId);
    }

    /**
     * StudyRequirement 단 건 조회
     */

    @GetMapping("/studies/{studyId}/requirements")
    @ResponseStatus(HttpStatus.OK)
    public List<StudyRequirementResponseDto> getAll(@PathVariable Long studyId) {
        return studyRequirementService.getAll(studyId);
    }
}
