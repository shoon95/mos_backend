package com.mos.backend.studybenefits.application.responsedto;

import com.mos.backend.studybenefits.entity.StudyBenefit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudyBenefitResponseDto {
    private Long id;
    private Long benefitNum;
    private String content;

    public static StudyBenefitResponseDto from(StudyBenefit studyBenefit) {
        StudyBenefitResponseDto benefitResponseDto = new StudyBenefitResponseDto();
        benefitResponseDto.id = studyBenefit.getId();
        benefitResponseDto.benefitNum = studyBenefit.getBenefitNum();
        benefitResponseDto.content = studyBenefit.getContent();
        return benefitResponseDto;
    }
}
