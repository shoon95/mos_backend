package com.mos.backend.studybenefits.presentation.controller.api;

import com.mos.backend.studybenefits.application.StudyBenefitService;
import com.mos.backend.studybenefits.application.responsedto.StudyBenefitResponseDto;
import com.mos.backend.studybenefits.presentation.requestdto.StudyBenefitRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class StudyBenefitController {

    private final StudyBenefitService studyBenefitService;

    @PostMapping("/studies/{studyId}/benefits")
    @ResponseStatus(HttpStatus.OK)
    public List<StudyBenefitResponseDto> createOrUpdateOrDelete(@PathVariable Long studyId, @RequestBody List<StudyBenefitRequestDto> benefitRequestDtoList) {
        return studyBenefitService.createOrUpdateOrDelete(studyId, benefitRequestDtoList);
    }

    @GetMapping("/studies/{studyId}/benefits")
    @ResponseStatus(HttpStatus.OK)
    public List<StudyBenefitResponseDto> getAll(@PathVariable Long studyId) {
        return studyBenefitService.getAll(studyId);
    }
}
