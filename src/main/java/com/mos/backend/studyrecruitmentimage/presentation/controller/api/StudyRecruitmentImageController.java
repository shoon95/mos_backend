package com.mos.backend.studyrecruitmentimage.presentation.controller.api;

import com.mos.backend.studymaterials.application.UploadType;
import com.mos.backend.studyrecruitmentimage.application.StudyRecruitmentImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class StudyRecruitmentImageController {

    private final StudyRecruitmentImageService studyRecruitmentImageService;

    @PostMapping("/recruitment-images")
    @ResponseStatus(HttpStatus.CREATED)
    public String temporaryUpload(
            @RequestParam("type") UploadType type,
            @RequestParam("file")MultipartFile file,
            @AuthenticationPrincipal Long userId) {
        return studyRecruitmentImageService.temporaryUpload(type, file, userId);
    }
}
