package com.mos.backend.studies.presentation.controller.api;

import com.mos.backend.studies.application.StudyService;
import com.mos.backend.studies.presentation.dto.StudyCreateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

    private static final String BASE_URL = "/studies/";

    private final StudyService studyService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid StudyCreateRequestDto requestDto, @AuthenticationPrincipal Long userId) {
        Long studyId = studyService.create(userId, requestDto);
        return ResponseEntity.created(URI.create(BASE_URL + studyId)).build();
    }
}
