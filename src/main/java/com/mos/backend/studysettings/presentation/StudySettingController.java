package com.mos.backend.studysettings.presentation;

import com.mos.backend.studysettings.application.StudySettingService;
import com.mos.backend.studysettings.application.res.StudySettingRes;
import com.mos.backend.studysettings.presentation.requestdto.StudySettingUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class StudySettingController {

    private final StudySettingService studySettingService;

    /**
     * 스터디 아이디로 해당 스터디 설정 조회
     * @param studyId
     * @return
     */
    @GetMapping("/studies/{studyId}/study-settings")
    @ResponseStatus(HttpStatus.OK)
    public StudySettingRes read(
            @PathVariable Long studyId
    ) {
        return studySettingService.read(studyId);
    }

    /**
     * 스터디 아이디로 조회해서 스터디 설정 수정
     * 지각 기준 시간, 결석 기준 시간 수정
     * @param studyId
     * @param requestDto
     * @return
     */
    @PatchMapping("/studies/{studyId}/study-settings")
    @ResponseStatus(HttpStatus.OK)
    public StudySettingRes update(
            @PathVariable Long studyId,
            @Valid @RequestBody StudySettingUpdateRequestDto requestDto
    ) {
        return studySettingService.update(studyId, requestDto.getLateThresholdMinutes(), requestDto.getAbsenceThresholdMinutes());
    }
}
