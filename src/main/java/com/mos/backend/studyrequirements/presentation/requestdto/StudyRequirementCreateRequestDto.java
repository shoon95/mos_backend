package com.mos.backend.studyrequirements.presentation.requestdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StudyRequirementCreateRequestDto {
    private Long id;
    @NotNull(message = "requirementNum은 필수입니다.")
    @Min(value = 1, message = "requirementNum은 1보다 커야합니다.")
    private Long requirementNum;
    @NotBlank(message = "content는 필수입니다.")
    private String content;
}
