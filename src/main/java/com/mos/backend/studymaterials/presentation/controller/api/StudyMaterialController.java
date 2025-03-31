package com.mos.backend.studymaterials.presentation.controller.api;

import com.mos.backend.studymaterials.application.StudyMaterialService;
import com.mos.backend.studymaterials.application.UploadType;
import com.mos.backend.studymaterials.application.responsedto.ReadAllStudyMaterialResponseDto;
import com.mos.backend.studymaterials.application.responsedto.ReadStudyMaterialResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class StudyMaterialController {

    private final StudyMaterialService studyMaterialService;

    @PostMapping("/studies/{studyId}/materials")
    @ResponseStatus(HttpStatus.OK)
    public ReadStudyMaterialResponseDto create(
            @PathVariable Long studyId,
            @RequestParam("file")MultipartFile file,
            @RequestParam("type")UploadType type,
            @AuthenticationPrincipal Long userId) {
        return studyMaterialService.create(studyId, userId, type, file);
    }

    @DeleteMapping("/studies/{studyId}/materials/{materialId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("studyId") Long studyId, @PathVariable("materialId") Long materialId) {
        studyMaterialService.delete(studyId, materialId);
    }

    @GetMapping("/studies/{studyId}/materials")
    @ResponseStatus(HttpStatus.OK)
    public ReadAllStudyMaterialResponseDto readAll(@PathVariable("studyId") Long studyId) {
        return studyMaterialService.readAll(studyId);
    }

    @GetMapping("/studies/{studyId}/materials/{materialId}")
    @ResponseStatus(HttpStatus.OK)
    public ReadStudyMaterialResponseDto read(@PathVariable("studyId") Long studyId, @PathVariable("materialId") Long materialId) {
        return studyMaterialService.read(studyId, materialId);
    }
}
