package com.mos.backend.studysettings.presentation.requestdto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudySettingUpdateRequestDto {
    @NotNull(message = "lateThresholdMinutes는 필수입니다.")
    @Positive(message = "lateThresholdMiniutes는 0보다 커야합니다.")
    private Integer lateThresholdMinutes;
    @NotNull(message = "absenceThresholdMinutes는 필수입니다.")
    @Positive(message = "absenceThresholdMinutes는 0보다 커야합니다.")
    private Integer absenceThresholdMinutes;
}
