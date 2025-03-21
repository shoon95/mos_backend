package com.mos.backend.userschedules.presentation.req;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserScheduleCreateReq {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    private String description;
    @NotNull(message = "시작 일시는 필수입니다.")
    @FutureOrPresent(message = "시작 일시는 현재 시각 이후여야 합니다.")
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @AssertTrue(message = "종료 일시가 존재한다면 시작 일시 이후여야 합니다.")
    public boolean isEndDateTimeAfterStartDateTime() {
        if (endDateTime == null) {
            return true;
        }
        return endDateTime.isAfter(startDateTime);
    }
}
