package com.mos.backend.studybenefits.presentation.requestdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudyBenefitRequestDto {
    private Long id;
    @NotNull(message = "benefitNum은 필수입니다.")
    @Min(value = 1, message = "benefitNum은 1보다 커야합니다.")
    private Long benefitNum;
    @NotNull(message = "benefit의 content는 필수입니다.")
    private String content;
}
