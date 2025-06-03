package com.mos.backend.users.presentation.controller.api;

import com.mos.backend.studies.application.StudyService;
import com.mos.backend.studymaterials.application.UploadType;
import com.mos.backend.users.application.UserService;
import com.mos.backend.users.application.responsedto.UserDetailRes;
import com.mos.backend.users.application.responsedto.UserStudiesResponseDto;
import com.mos.backend.users.presentation.requestdto.UserUpdateReq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    private final StudyService studyService;

    @PatchMapping
    public ResponseEntity<Void> updateProfileInfo(@AuthenticationPrincipal Long userId, @Valid @RequestBody UserUpdateReq req) {
        userService.updateProfileInfo(userId, req);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/images")
    public void updateProfileImage(@AuthenticationPrincipal Long userId, @RequestPart("file") MultipartFile file, @RequestParam("type") UploadType type) {
        userService.updateProfileImage(userId, file, type);
    }

    @GetMapping
    public ResponseEntity<UserDetailRes> getDetail(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(userService.getDetail(userId));
    }

    @GetMapping("/tokens")
    public void reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        userService.reissueTokens(request, response);
    }

    @GetMapping("/{userId}/studies")
    public List<UserStudiesResponseDto> readUserStudies(
            @PathVariable Long userId,
            @RequestParam(required = false) String progressStatus,
            @RequestParam(required = false) String participationStatus,
            @AuthenticationPrincipal Long currentUserId) {
        return studyService.readUserStudies(userId, progressStatus, participationStatus, currentUserId);
    }
}
