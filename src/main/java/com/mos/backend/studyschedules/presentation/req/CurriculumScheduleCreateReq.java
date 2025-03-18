package com.mos.backend.studyschedules.presentation.req;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CurriculumScheduleCreateReq {
    @NotNull(message = "시작 일시는 필수입니다.")
    @FutureOrPresent(message = "시작 일시는 현재 시각 이후여야 합니다.")
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
