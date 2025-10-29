package com.mos.backend.studynotices.presentation.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyNoticeUpdateRequestDto {

    @NotBlank(message = "title 비어있거나 공백일 수 없습니다.")
    private String title;
    private String content;
    @NotNull(message = "pinned는 비어있거나 공백일 수 없습니다.")
    private Boolean pinned;
    @NotNull(message = "pinned는 비어있거나 공백일 수 없습니다.")
    private Boolean important;

}
