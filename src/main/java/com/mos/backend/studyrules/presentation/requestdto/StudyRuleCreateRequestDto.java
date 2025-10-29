package com.mos.backend.studyrules.presentation.requestdto;

import jakarta.validation.constraints.Min;
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
public class StudyRuleCreateRequestDto {
    private Long id;
    @NotNull(message = "ruleNum은 필수입니다.")
    @Min(value = 1, message = "ruleNum은 1보다 커야합니다.")
    private Long ruleNum;
    @NotBlank(message = "content는 필수입니다.")
    private String content;
}
