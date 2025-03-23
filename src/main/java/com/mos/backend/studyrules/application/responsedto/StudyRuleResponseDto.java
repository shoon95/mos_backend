package com.mos.backend.studyrules.application.responsedto;

import com.mos.backend.studyrules.entity.StudyRule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudyRuleResponseDto {
    private Long id;
    private Long ruleNum;
    private String content;

    public static StudyRuleResponseDto from(StudyRule studyRule) {
        StudyRuleResponseDto studyRuleResponseDto = new StudyRuleResponseDto();
        studyRuleResponseDto.id = studyRule.getId();
        studyRuleResponseDto.ruleNum = studyRule.getRuleNum();
        studyRuleResponseDto.content = studyRule.getContent();
        return studyRuleResponseDto;
    }
}
