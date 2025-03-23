package com.mos.backend.studyrules.presentation.controller.api;

import com.mos.backend.studyrules.application.StudyRuleService;
import com.mos.backend.studyrules.application.responsedto.StudyRuleResponseDto;
import com.mos.backend.studyrules.presentation.requestdto.StudyRuleCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyRuleController {

    private final StudyRuleService studyRuleService;

    @PostMapping("/studies/{studyId}/rules")
    @ResponseStatus(HttpStatus.OK)
    public List<StudyRuleResponseDto> createOrUpdateOrDelete(@PathVariable Long studyId, @RequestBody List<StudyRuleCreateRequestDto> requestDtoList) {
        return studyRuleService.createOrUpdateOrDelete(studyId, requestDtoList);
    }

    @GetMapping("/studies/{studyId}/rules")
    @ResponseStatus(HttpStatus.OK)
    public List<StudyRuleResponseDto> getAll(@PathVariable Long studyId) {
        return studyRuleService.getAll(studyId);
    }

    @GetMapping("/studies/{studyId}/rules/{studyRuleId}")
    @ResponseStatus(HttpStatus.OK)
    public StudyRuleResponseDto get(@PathVariable Long studyId, @PathVariable Long studyRuleId) {
        return studyRuleService.get(studyId, studyRuleId);
    }
}
